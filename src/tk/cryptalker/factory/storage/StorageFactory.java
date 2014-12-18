package tk.cryptalker.factory.storage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.activity.AbstractActivity;
import tk.cryptalker.activity.DashboardActivity;
import tk.cryptalker.activity.HomeActivity;
import tk.cryptalker.application.CrypTalkerApplication;
import tk.cryptalker.manager.RequestManager;
import tk.cryptalker.model.Message;
import tk.cryptalker.model.Response;
import tk.cryptalker.model.Room;
import tk.cryptalker.model.UserInfo;

import java.io.*;
import java.util.ArrayList;

public class StorageFactory
{

    private static final String TAG = "StorageFactory";
    private static final String P_APP_VERSION = "appVersion";
    public static final String P_REG_ID = "registration_id";
    public static final String P_TOKEN = "token";

    public static final String STORAGE_FILE = "cryptalkers.storage";
    public static final String P_USER = "user";
    public static final String P_FRIEND_REQUEST_RECEIVED = "friend_request_received";
    public static final String P_FRIEND_REQUEST_SENT = "friend_request_sent";
    public static final String P_ROOMS = "rooms";

    /**
     * Stores the registration ID and the app versionCode in the application's
     */
    public static void storeRegistrationId(String regId) {

        final SharedPreferences prefs = getPreferences();
        int appVersion = getAppVersion();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(P_REG_ID, regId);
        editor.putInt(P_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * If result is empty, the app needs to register.
     */
    public static String getRegistrationId() {

        final SharedPreferences prefs = getPreferences();
        String registrationId = prefs.getString(P_REG_ID, "");

        if (registrationId.isEmpty()) {
            return "";
        }

        int registeredVersion = prefs.getInt(P_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();

        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }


    public static SharedPreferences getPreferences() {
        return AbstractActivity.getContext().getSharedPreferences("CrypTalker", Context.MODE_PRIVATE);
    }

    public static int getAppVersion() {

        Context context = AbstractActivity.getContext();

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static void getUserInfo() {
        final Context context = AbstractActivity.getContext();

        File f = context.getFileStreamPath(STORAGE_FILE);
        if (f.length() > 0) {

            initialiseUserInfo();

            Intent intent = new Intent(context, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            return;
        }

        RequestManager.getInstance(DashboardActivity.getContext()).getUserInfo(new com.android.volley.Response.Listener<Response>() {

            @Override
            public void onResponse(Response response) {

                if (response.isSuccess()) {

                    try {
                        storeUserInfo(response.getData());

                        Log.i(TAG, response.getData().toString());

                        Intent intent = new Intent(context, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    } catch (JSONException e) {
                        Log.i(TAG, "JSON Exception on user getUserInfo return parsing");
                    }
                } else {
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error during the request => " + error.toString());
            }
        });
    }

    /**
     * Get users info and clean merge them to the actual userInfo (because the server can't send us the room's messages)
     */
    public static void refreshUserInfo() {

        final Context context = AbstractActivity.getContext();

        // CHeck if we already have userinfos
        final UserInfo userInfo = CrypTalkerApplication.getUserInfo();

        if (userInfo == null) {
            getUserInfo();
            return;
        }

        RequestManager.getInstance(DashboardActivity.getContext()).getUserInfo(new com.android.volley.Response.Listener<Response>() {

            @Override
            public void onResponse(Response response) {

                if (response.isSuccess()) {

                    try {
                        // We store the new user infos
                        storeUserInfo(response.getData());

                        // We add the message (which are only in the application) to the new userInfo
                        UserInfo newUserInfo = CrypTalkerApplication.getUserInfo();
                        ArrayList<Room> newRooms = newUserInfo.getRooms();
                        ArrayList<Room> oldRooms = userInfo.getRooms();

                        for (int i = 0; i < newRooms.size(); i++) {

                            Room newRoom = newRooms.get(i);

                            int roomId = newRoom.getId();

                            ArrayList<Message> oldMessages = new ArrayList<Message>();

                            for (int j = 0; j < oldRooms.size(); j++) {

                                Room oldRoom = oldRooms.get(j);

                                if (oldRoom.getId() == roomId) {
                                    oldMessages = oldRoom.getMessages();
                                }
                            }

                            newRoom.setMessages(oldMessages);
                            newRooms.set(i, newRoom);
                        }

                        newUserInfo.setRooms(newRooms);

                        // Set newUserInfo (with old messages merged) to application
                        CrypTalkerApplication.setUserInfo(newUserInfo);

                    } catch (JSONException e) {
                        Log.i(TAG, "JSON Exception on user getUserInfo return parsing");
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error during the request => " + error.toString());
            }
        });
    }

    public static void storeToken(String token) {

        final SharedPreferences prefs = getPreferences();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(P_TOKEN, token);
        editor.apply();
    }

    public static String getToken() {
        final SharedPreferences prefs = getPreferences();
        String token = prefs.getString(P_TOKEN, "");

        return token;
    }

    public static void storeUserInfo(JSONObject data) throws JSONException {
        FileOutputStream outputStream;
        Context context = AbstractActivity.getContext();

        try {
            outputStream = context.openFileOutput(STORAGE_FILE, Context.MODE_PRIVATE);
            outputStream.write(data.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialiseUserInfo();
    }

    private static void initialiseUserInfo() {

        Context context = AbstractActivity.getContext();

        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(STORAGE_FILE)));
            String inputString;
            StringBuffer stringBuffer = new StringBuffer();

            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString);
            }

            try {
                JSONObject obj = new JSONObject(stringBuffer.toString());

                UserInfo userInfo = new UserInfo();
                userInfo.setUser(obj.optJSONObject(P_USER));
                userInfo.setFriendRequestReceived(obj.optJSONArray(P_FRIEND_REQUEST_RECEIVED));
                userInfo.setRooms(obj.optJSONArray(P_ROOMS));

                CrypTalkerApplication.setUserInfo(userInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package tk.cryptalker.factory.storage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.activity.DashboardActivity;
import tk.cryptalker.activity.HomeActivity;
import tk.cryptalker.manager.RequestManager;
import tk.cryptalker.model.Response;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class StorageFactory
{
    private static final String TAG = "StorageFactory";
    private static final String P_APP_VERSION = "appVersion";
    public static final String P_REG_ID = "registration_id";
    public static final String P_TOKEN = "token";

    public static final String STORAGE_FILE = "cryptalkers.storage";
    public static final String P_FRIEND_REQUEST_RECEIVED = "friend_request_received";
    public static final String P_FRIEND_REQUEST_SENDED = "friend_request_sended";
    public static final String P_ROOMS = "rooms";

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    public static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getPreferences(context);
        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(P_REG_ID, regId);
        editor.putInt(P_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getPreferences(context);
        String registrationId = prefs.getString(P_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(P_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("CrypTalker", Context.MODE_PRIVATE);
    }
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static void getUserInfo(final Context context)
    {
        RequestManager.getInstance(DashboardActivity.getContext()).getUserInfo(new com.android.volley.Response.Listener<Response>() {

            @Override
            public void onResponse(Response response) {

                if (response.isSuccess()) {

                    try {
                        storeUserInfo(response.getData(), context);

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

    public static void storeToken(String token, Context context) {
        final SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(P_TOKEN, token);
        editor.commit();
    }

    public static String getToken(Context context) {
        final SharedPreferences prefs = getPreferences(context);
        String token = prefs.getString(P_TOKEN, "");

        return token;
    }

    public static void storeUserInfo(JSONObject data, Context context) throws JSONException {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(STORAGE_FILE, Context.MODE_PRIVATE);
            outputStream.write(data.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONArray getUserInfo(String key, Context context) {

        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    context.openFileInput(STORAGE_FILE)));
            String inputString;
            StringBuffer stringBuffer = new StringBuffer();
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }

            try {
                JSONObject obj = new JSONObject(stringBuffer.toString());
                return obj.getJSONArray(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

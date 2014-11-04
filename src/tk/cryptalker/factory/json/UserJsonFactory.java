package tk.cryptalker.factory.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import tk.cryptalker.model.User;

public class UserJsonFactory {

    private static final String TAG = "TokenJsonFactory";

    public static JSONObject getJSONObject(User u) throws JSONException
    {

        if (u == null){
            Log.e(TAG, "Unable to create JSONObject from User caused by User null");
            return null;
        }

        JSONObject result = new JSONObject();
        result.accumulate("userID", u.getUserID());
        result.accumulate("userPseudo", u.getUserPseudo());
        result.accumulate("userHash", u.getUserHash());
        result.accumulate("userCreationDate", u.getUserCreationDate());
        return result;
    }

    public static JSONArray getJSONArray(ArrayList<User> users) throws JSONException
    {
        if (users == null){
            Log.e(TAG, "Unable to create JSONArray from User list caused by User list null");
            return null;
        }
        JSONArray result = new JSONArray();
        for (User u : users){
            result.put(UserJsonFactory.getJSONObject(u));
        }
        return result;
    }

    public static User parseFromJSONObject(JSONObject json) throws JSONException
    {

        if (json == null){
            Log.e(TAG, "Unable to create Comment from Json caused by json null");
            return null;
        }

        User result = new User();

        result.setUserCreationDate(json.getLong("userCreationDate"));
        result.setUserID(json.getLong("userID"));
        result.setUserPseudo(json.getString("userPseudo"));

        return result;
    }

    public static ArrayList<User> parseFromJSONArray(JSONArray array) throws JSONException
    {

        if (array == null){
            Log.e(TAG, "Unable to create Comment List from Json caused by json null");
            return null;
        }

        ArrayList<User> result = new ArrayList<User>();
        int length = array.length();
        for (int i = 0 ; i < length ; i++){
            result.add(UserJsonFactory.parseFromJSONObject(array.getJSONObject(i)));
        }
        return  result;

    }

}

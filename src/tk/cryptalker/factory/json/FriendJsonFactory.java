package tk.cryptalker.factory.json;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.model.Friend;

import java.util.ArrayList;

public class FriendJsonFactory
{

    private static final String TAG = "TokenJsonFactory";

    public static Friend parseFromJSONObject(JSONObject json) throws JSONException
    {

        if (json == null){
            Log.e(TAG, "Unable to create Comment from Json caused by json null");
            return null;
        }

        Friend result = new Friend();

        result.setId(json.getLong("id"));
        result.setPseudo(json.getString("pseudo"));

        return result;
    }

    public static ArrayList<Friend> parseFromJSONArray(JSONArray array) throws JSONException
    {

        if (array == null){
            Log.e(TAG, "Unable to create Comment List from Json caused by json null");
            return null;
        }

        ArrayList<Friend> result = new ArrayList<Friend>();
        int length = array.length();
        for (int i = 0 ; i < length ; i++){
            result.add(FriendJsonFactory.parseFromJSONObject(array.getJSONObject(i)));
        }
        return  result;

    }

}

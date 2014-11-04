package tk.cryptalker.factory.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import tk.cryptalker.model.Response;
import tk.cryptalker.model.ResponseCode;

public class ResponseJsonFactory {

    private static final String TAG = "TokenJsonFactory";

    public static JSONObject getJSONObject(Response r) throws JSONException{

        if (r == null){
            Log.e(TAG, "Unable to create JSONObject from Response caused by Response null");
            return null;
        }

        JSONObject result = new JSONObject();
        result.accumulate("code", r.getCode());
        result.accumulate("status", r.getStatus());
        return result;
    }

    public static Response parseFromJSONObject(JSONObject json) throws JSONException{

        if (json == null){
            Log.e(TAG, "Unable to create Response from Json caused by json null");
            return null;
        }

        Response result = new Response();

        result.setCode(ResponseCode.valueOf(json.getString("code")));
        result.setStatus(json.getString("status"));

        return result;
    }

}

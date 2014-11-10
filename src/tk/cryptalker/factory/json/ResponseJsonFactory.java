package tk.cryptalker.factory.json;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import tk.cryptalker.model.Response;

public class ResponseJsonFactory
{

    private static final String TAG = "ResponseJsonFactory";

    public static JSONObject getJSONObject(Response r) throws JSONException{

        if (r == null){
            Log.e(TAG, "Unable to create JSONObject from Response caused by Response null");
            return null;
        }

        JSONObject result = new JSONObject();
        result.accumulate("data", r.getData());
        result.accumulate("success", r.isSuccess());
        result.accumulate("errors", r.getErrors());
        return result;
    }

    public static Response parseFromJSONObject(JSONObject json) throws JSONException{

        if (json == null){
            Log.e(TAG, "Unable to create Response from Json caused by json null");
            return null;
        }

        Response result = new Response();

        result.setData(json.getJSONObject("data"));
        result.setSuccess(json.getBoolean("success"));
        result.setErrors(json.getJSONObject("errors"));

        return result;
    }

    public static JSONObject getRequestJSONObject(Object object) throws JSONException
    {
        Gson gson = new Gson();

        if (object == null){
            Log.e(TAG, "Unable to create JSONObject from class caused by class null");
            return null;
        }

        String jsonAsString = gson.toJson(object);

        return new JSONObject(jsonAsString);
    }

    public static Object parseRequestFromJSONObject(JSONObject json, Class object) throws JSONException
    {
        Gson gson = new Gson();

        if (json == null){
            Log.e(TAG, "Unable to create Comment from Json caused by json null");
            return null;
        }

        return gson.fromJson(json.toString(), object);
    }
}

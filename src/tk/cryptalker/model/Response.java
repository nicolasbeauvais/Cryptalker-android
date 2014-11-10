package tk.cryptalker.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Response
{

    protected static final String TAG = "Response";

    private JSONObject  data;
    private boolean success;
    private JSONObject errors;

    public static Response parseFromJSONObject(Response r, JSONObject json) throws JSONException
    {

        if (json == null){
            Log.e(TAG, "Unable to create Response from Json caused by json null");
            return null;
        }

        if (r == null){
            Log.e(TAG, "Unable to create Response from Json caused by Response null");
            return null;
        }

        r.setSuccess(json.getBoolean("success"));
        return r;
    }

    public static Response parseFromJSONObject(JSONObject json) throws JSONException
    {
        if (json == null){
            Log.e(TAG, "Unable to create Response from Json caused by json null");
            return null;
        }

        Response r = new Response();

        r.setData(json.getJSONObject("data"));
        r.setSuccess(json.getBoolean("success"));
        r.setErrors(json.getJSONObject("errors"));

        return r;
    }

    public JSONObject getData()
    {
        return data;
    }

    public void setData(JSONObject data)
    {
        this.data = data;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public JSONObject getErrors()
    {
        return errors;
    }

    public void setErrors(JSONObject  errors)
    {
        this.errors = errors;
    }
}

package tk.cryptalker.request;

import android.util.Log;
import com.android.volley.Request;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import tk.cryptalker.factory.json.UserJsonFactory;
import tk.cryptalker.model.User;

public class CreateUserRequest extends AbstractRequest {

    private static final String REST = "users/register";

    public CreateUserRequest(Context context, User data, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException {
        super(context, Request.Method.POST, constructUrl(), constructJSONObject(data), listener, errorListener);
    }

    private static String constructUrl(){
        Log.i("URL", SERVER_URL + REST);
        return SERVER_URL + REST;
    }

    private static JSONObject constructJSONObject(User data) throws JSONException{
        JSONObject json = UserJsonFactory.getJSONObject(data);
        return json;
    }
}

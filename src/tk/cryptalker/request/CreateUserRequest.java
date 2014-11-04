package tk.cryptalker.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import tk.cryptalker.factory.json.UserJsonFactory;
import tk.cryptalker.model.User;

public class CreateUserRequest extends AbstractRequest{

    private static final String CREATE_USER = "create_new_user";

    public CreateUserRequest(Context context, int method, User data, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException {
        super(context, method, constructUrl(), constructJSONObject(data), listener, errorListener);
    }

    public CreateUserRequest(Context context, User data, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException {
        super(context,constructUrl(), constructJSONObject(data), listener, errorListener);
    }

    private static String constructUrl(){
        return SERVER_URL + CREATE_USER;
    }

    private static JSONObject constructJSONObject(User data) throws JSONException{
        JSONObject json = UserJsonFactory.getJSONObject(data);
        return json;
    }


}

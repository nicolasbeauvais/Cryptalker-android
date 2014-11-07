package tk.cryptalker.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import org.json.JSONException;
import org.json.JSONObject;

import tk.cryptalker.model.Response;
import tk.cryptalker.model.User;
import tk.cryptalker.request.CreateUserRequest;
import tk.cryptalker.request.LoginRequest;
import tk.cryptalker.request.LoginWithTokenRequest;

public class RequestManager {

    private static final String TAG = "RequestManager";

    /**
     * The unique instance of the manager.
     */
    private static RequestManager SINGLETON = null;

    /**
     * The lock for thread safety.
     */
    private static final Object __synchronizedObject = new Object();

    private Context context;

    private static int requestId = -1;

    public static RequestManager getInstance(Context context) {

        if (SINGLETON == null) {
            synchronized (__synchronizedObject) {
                if (SINGLETON == null) {
                    SINGLETON = new RequestManager(context);
                }
            }
        }
        return SINGLETON;
    }

    private RequestManager(Context context) {
        this.context = context;
    }

    public static int getRequestId(){
        return requestId++;
    }

    public void createUser(User user, final Listener<Response> listener, ErrorListener errorListener) throws JSONException {

        CreateUserRequest request = new CreateUserRequest(context, user, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject arg0) {
                Response response = null;
                try {
                    response = Response.parseFromJSONObject(arg0);
                } catch (JSONException e) {
                    Log.e(TAG, "An error occurred parsing create user response", e);
                }

                if (listener != null){
                    listener.onResponse(response);
                }
            }
        }, errorListener);

        request.start();
    }

    public void loginUser(User user, final Listener<Response> listener, ErrorListener errorListener) throws JSONException {

        LoginRequest request = new LoginRequest(context, user, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject arg0) {
                Response response = null;
                try {
                    response = Response.parseFromJSONObject(arg0);
                } catch (JSONException e) {
                    Log.e(TAG, "An error occurred parsing login user response", e);
                }

                if (listener != null){
                    listener.onResponse(response);
                }
            }
        }, errorListener);

        request.start();
    }

    public void loginWithTokenUser(User user, final Listener<Response> listener, ErrorListener errorListener) throws JSONException {

        LoginWithTokenRequest request = new LoginWithTokenRequest(context, user, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject arg0) {
                Response response = null;
                try {
                    response = Response.parseFromJSONObject(arg0);
                } catch (JSONException e) {
                    Log.e(TAG, "An error occurred parsing loginWithToken user response", e);
                }

                if (listener != null){
                    listener.onResponse(response);
                }
            }
        }, errorListener);

        request.start();
    }
}

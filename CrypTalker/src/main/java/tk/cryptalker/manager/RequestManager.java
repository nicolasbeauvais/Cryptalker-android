package tk.cryptalker.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import org.json.JSONException;
import org.json.JSONObject;

import tk.cryptalker.factory.json.ResponseJsonFactory;
import tk.cryptalker.model.Friend;
import tk.cryptalker.model.RequestConstructor;
import tk.cryptalker.model.Response;
import tk.cryptalker.model.User;
import tk.cryptalker.request.*;

import java.lang.reflect.Constructor;

public class RequestManager
{

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

    public static RequestManager getInstance(Context context)
    {

        if (SINGLETON == null) {
            synchronized (__synchronizedObject) {
                if (SINGLETON == null) {
                    SINGLETON = new RequestManager(context);
                }
            }
        }
        return SINGLETON;
    }

    private RequestManager(Context context)
    {
        this.context = context;
    }

    public static int getRequestId()
    {
        return requestId++;
    }

    private void requestAbstracter(Object data, RequestConstructor rc, ErrorListener errorListener)
    {
        try {
            AbstractRequest ar = new AbstractRequest(context, rc.getVerb(), AbstractRequest.makeUrl(rc.getRest()), AbstractRequest.getRequestJSONObject(data), rc.getListener(), errorListener);
            ar.start();
        } catch (JSONException e) {
            Log.e(TAG, "An error occurred during requestAbstracter method", e);
        }
    }

    private Listener getGenericListener(final Listener listener)
    {
        return new Listener<JSONObject>() {

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
        };
    }

    public void createUser(User user, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.POST);
        requestConstructor.setRest("users/register");
        requestConstructor.setListener(getGenericListener(listener));

        requestAbstracter(user, requestConstructor, errorListener);
    }

    public void loginUser(User user, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.POST);
        requestConstructor.setRest("users/login");
        requestConstructor.setListener(getGenericListener(listener));

        requestAbstracter(user, requestConstructor, errorListener);
    }

    public void loginWithTokenUser(User user, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.POST);
        requestConstructor.setRest("users/login-with-token");
        requestConstructor.setListener(getGenericListener(listener));

        requestAbstracter(user, requestConstructor, errorListener);
    }

    public void AddFriendRequest(Friend friend, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.POST);
        requestConstructor.setRest("friends/request");
        requestConstructor.setListener(getGenericListener(listener));

        requestAbstracter(friend, requestConstructor, errorListener);
    }
}

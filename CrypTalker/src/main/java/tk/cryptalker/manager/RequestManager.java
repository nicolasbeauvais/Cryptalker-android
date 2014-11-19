package tk.cryptalker.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import org.json.JSONException;
import org.json.JSONObject;

import tk.cryptalker.R;
import tk.cryptalker.activity.AbstractActivity;
import tk.cryptalker.model.*;
import tk.cryptalker.request.*;

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
        JSONObject jsonData = null;

        try {
            if (data != null) {
                jsonData = AbstractRequest.getRequestJSONObject(data);
            }
        } catch (JSONException e) {
            Log.e(TAG, "An error occurred during requestAbstracter method", e);
        }

        AbstractRequest ar = new AbstractRequest(context, rc.getVerb(), AbstractRequest.makeUrl(rc.getRest()), jsonData,
                getGenericListener(rc), errorListener);
        ar.start();
    }

    private Listener getGenericListener(final RequestConstructor rc)
    {
        if (rc.isProgressDialog()) {
            AbstractActivity.showProgress(rc.getProgressDialogTitle(), rc.getProgressDialogMessage());
        }

        return new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject arg0) {

                Response response = null;
                try {
                    response = Response.parseFromJSONObject(arg0);
                } catch (JSONException e) {
                    Log.e(TAG, "An error occurred parsing create user response", e);
                }

                if (rc.getListener() != null){
                    rc.getListener().onResponse(response);
                }

                if (rc.isProgressDialog()) {
                    AbstractActivity.hideProgress();
                }
            }
        };
    }

    public void createUser(User user, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.POST);
        requestConstructor.setRest("users/register");
        requestConstructor.setListener(listener);
        requestConstructor.setProgressDialogMessage(R.string.dialog_progress_create_account_message);

        requestAbstracter(user, requestConstructor, errorListener);
    }

    public void loginUser(User user, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.POST);
        requestConstructor.setRest("users/login");
        requestConstructor.setListener(listener);
        requestConstructor.setProgressDialogMessage(R.string.dialog_progress_login_message);

        requestAbstracter(user, requestConstructor, errorListener);
    }

    public void loginWithTokenUser(User user, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.POST);
        requestConstructor.setRest("users/login-with-token");
        requestConstructor.setListener(listener);
        requestConstructor.setProgressDialog(false);

        requestAbstracter(user, requestConstructor, errorListener);
    }

    public void getUserInfo(final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.GET);
        requestConstructor.setRest("users/info");
        requestConstructor.setListener(listener);
        requestConstructor.setProgressDialog(false);

        requestAbstracter(null, requestConstructor, errorListener);
    }

    public void addFriendRequest(Friend friend, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.POST);
        requestConstructor.setRest("friends/request");
        requestConstructor.setListener(listener);
        requestConstructor.setProgressDialogMessage(R.string.dialog_progress_add_friend_message);

        requestAbstracter(friend, requestConstructor, errorListener);
    }

    public void acceptFriendRequest(int user_id, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.GET);
        requestConstructor.setRest("friends/accept/" + String.valueOf(user_id));
        requestConstructor.setListener(listener);
        requestConstructor.setProgressDialogMessage(R.string.dialog_progress_accept_friend_message);

        requestAbstracter(null, requestConstructor, errorListener);
    }

    public void denyFriendRequest(int user_id, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.GET);
        requestConstructor.setRest("friends/refuse/" + String.valueOf(user_id));
        requestConstructor.setListener(listener);
        requestConstructor.setProgressDialogMessage(R.string.dialog_progress_refuse_friend_message);

        requestAbstracter(null, requestConstructor, errorListener);
    }

    public void sendMessageRequest(Message message, final Listener<Response> listener, ErrorListener errorListener)
    {
        RequestConstructor requestConstructor = new RequestConstructor();

        requestConstructor.setVerb(Request.Method.POST);
        requestConstructor.setRest("messages/new");
        requestConstructor.setListener(listener);
        requestConstructor.setProgressDialog(false);

        requestAbstracter(message, requestConstructor, errorListener);
    }
}

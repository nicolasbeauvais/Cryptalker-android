package tk.cryptalker.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import de.greenrobot.event.EventBus;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.activity.ChatActivity;
import tk.cryptalker.factory.storage.StorageFactory;
import tk.cryptalker.model.UserInfo;

public class CrypTalkerApplication extends Application
{
    private static UserInfo userInfo;

    private static EventBus messageEvent;

    @Override
    public void onCreate() {
        mInstance = this;

//        EventBus.getDefault().register(ChatActivity.class);
    }

    private static final String TAG = "CrypTalkerApplication";

    private RequestQueue mRequestQueue;

    private static CrypTalkerApplication mInstance;

    public static synchronized CrypTalkerApplication getInstance()
    {
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null) {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            CookieStore cookieStore = new BasicCookieStore();
            httpClient.setCookieStore(cookieStore);

            HttpStack httpStack = new HttpClientStack(httpClient);

            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), httpStack);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        CrypTalkerApplication.userInfo = userInfo;
    }

    public static void commitUserInfo()
    {
        try {
            StorageFactory.storeUserInfo(new JSONObject(new Gson().toJson(CrypTalkerApplication.getUserInfo())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static EventBus getMessageEvent() {
        return messageEvent;
    }
}

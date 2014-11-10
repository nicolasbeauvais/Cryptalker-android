package tk.cryptalker.request;

import android.content.Context;
import android.util.Log;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.factory.json.FriendJsonFactory;
import tk.cryptalker.model.Friend;

public class AddFriendRequest extends AbstractRequest
{

    private static final String REST = "friends/request";

    public AddFriendRequest(Context context, Friend data, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException
    {
        super(context, Method.POST, constructUrl(), constructJSONObject(data), listener, errorListener);
    }

    private static String constructUrl()
    {
        Log.i("URL", SERVER_URL + REST);
        return SERVER_URL + REST;
    }

    private static JSONObject constructJSONObject(Friend data) throws JSONException
    {
        JSONObject json = FriendJsonFactory.getJSONObject(data);
        return json;
    }
}

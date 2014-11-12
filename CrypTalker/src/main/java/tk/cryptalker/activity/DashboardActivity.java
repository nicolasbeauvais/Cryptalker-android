package tk.cryptalker.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.R;
import tk.cryptalker.dialog.AddFriendDialogFragment;

public class DashboardActivity extends AbstractActivity
{
    private static final String TAG = "DashboardActivity";

    private MenuItem menuItem;

    private JSONArray friend_request_received;
    private JSONArray friend_request_sended;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeLayout(R.layout.activity_dashboard, R.string.dashboard_title, R.menu.dashboard);

        initView();
    }

    private void initView()
    {
        friend_request_received = getUserInfo(P_FRIEND_REQUEST_RECEIVED);
        friend_request_sended = getUserInfo(P_FRIEND_REQUEST_SENDED);

        RelativeLayout wrapper_friend_request_received = (RelativeLayout)findViewById(R.id.friend_request_received);
        RelativeLayout wrapper_friend_request_sended = (RelativeLayout)findViewById(R.id.friend_request_sended);

        try {

            // Display Friend request received
            for (int i = 0; i < friend_request_received.length(); i++) {

                JSONObject friend = friend_request_received.getJSONObject(i);
                int id = friend.getInt("id");
                String pseudo = friend.getString("pseudo");

                TextView textView = new TextView(this);
                textView.setText(pseudo);

                wrapper_friend_request_received.addView(textView);
            }

            // Display Friend request sended
            for (int i = 0; i < friend_request_sended.length(); i++) {

                JSONObject friend = friend_request_sended.getJSONObject(i);
                int id = friend.getInt("id");
                String pseudo = friend.getString("pseudo");

                TextView textView = new TextView(this);
                textView.setText(pseudo);

                wrapper_friend_request_sended.addView(textView);
            }
        } catch (JSONException e) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        Boolean value = super.onCreateOptionsMenu(menu);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                menuItem = menu.findItem(R.id.new_friend_request);
                menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        AddFriendDialogFragment addFriendDialogFragment = new AddFriendDialogFragment();
                        addFriendDialogFragment.show(getFragmentManager(), "AddFriendDialogFragment");

                        return true;
                    }
                });
            }
        });

        return value;
    }
}

package tk.cryptalker.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.R;
import tk.cryptalker.adapter.CustomListAdapter;
import tk.cryptalker.fragment.AddFriendDialogFragment;
import tk.cryptalker.model.Room;
import tk.cryptalker.model.User;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AbstractActivity
{
    private static final String TAG = "DashboardActivity";

    private MenuItem menuItem;

    private ListView listView;
    private CustomListAdapter adapter;
    private JSONArray friend_request_received;
    private List<Room> roomList = new ArrayList<Room>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeLayout(R.layout.activity_dashboard, R.string.dashboard_title, R.menu.dashboard);

        initView();
    }

    private void initView()
    {
        friend_request_received = getUserInfo(P_FRIEND_REQUEST_RECEIVED);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, roomList);
        listView.setAdapter(adapter);

        for (int i = 0; i < friend_request_received.length(); i++) {

            try {
                JSONObject obj = friend_request_received.getJSONObject(i);
                Room room = new Room();

                Log.i(TAG, obj.toString());

                room.setName(obj.getString("pseudo"));
                room.setInvite(true);

                roomList.add(room);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter.notifyDataSetChanged();
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

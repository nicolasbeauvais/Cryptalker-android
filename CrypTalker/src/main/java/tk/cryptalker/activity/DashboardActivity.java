package tk.cryptalker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.R;
import tk.cryptalker.adapter.CustomListAdapter;
import tk.cryptalker.factory.storage.StorageFactory;
import tk.cryptalker.fragment.AddFriendDialogFragment;
import tk.cryptalker.model.Room;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AbstractActivity
{
    private static final String TAG = "DashboardActivity";

    private MenuItem menuItem;

    private ListView listView;
    public static CustomListAdapter adapter;
    private JSONArray friend_request_received;
    private JSONArray rooms;
    public static List<Room> roomList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeLayout(R.layout.activity_dashboard, getString(R.string.dashboard_title), R.menu.dashboard);

        initView();
    }

    private void initView()
    {
        friend_request_received = StorageFactory.getUserInfo(StorageFactory.P_FRIEND_REQUEST_RECEIVED);
        rooms = StorageFactory.getUserInfo(StorageFactory.P_ROOMS);

        roomList = new ArrayList<Room>();

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, roomList);
        listView.setAdapter(adapter);

        for (int i = 0; i < friend_request_received.length(); i++) {

            try {
                JSONObject obj = friend_request_received.getJSONObject(i);
                Room room = new Room();

                room.setName(obj.getString("pseudo"));
                room.setInvite(true);
                room.setInviteId(obj.getInt("id"));

                roomList.add(room);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < rooms.length(); i++) {

            try {
                JSONObject obj = rooms.getJSONObject(i);
                Room room = new Room();

                room.setId(obj.getInt("room_id"));
                room.setName(obj.getString("name"));
                room.setInvite(false);

                roomList.add(room);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long lo){

                Room item = (Room)adapter.getItem(position);

                Intent intent = new Intent(context, ChatActivity.class);
                Bundle b = new Bundle();
                b.putInt("roomId", item.getId());
                b.putString("roomName", item.getName());
                intent.putExtras(b);
                startActivity(intent);
            }


        });

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

                        AddFriendDialogFragment addFriendDialogFragment = new AddFriendDialogFragment(DashboardActivity.this);
                        addFriendDialogFragment.show(getFragmentManager(), "AddFriendDialogFragment");

                        return true;
                    }
                });
            }
        });

        return value;
    }
}

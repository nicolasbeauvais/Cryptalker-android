package tk.cryptalker.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import de.greenrobot.event.EventBus;
import tk.cryptalker.R;
import tk.cryptalker.adapter.ChatListAdapter;
import tk.cryptalker.application.CrypTalkerApplication;
import tk.cryptalker.event.MessageEvent;
import tk.cryptalker.factory.valdiation.ValidationFactory;
import tk.cryptalker.manager.RequestManager;
import tk.cryptalker.model.Message;
import tk.cryptalker.model.Response;
import tk.cryptalker.model.UserInfo;


import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AbstractActivity
{
    private static final String TAG = "ChatActivity";

    private static boolean active;
    private static int roomId;

    private String roomName;

    ImageButton sendMessage;
    TextView message;
    private ArrayList<TextView> inputs = new ArrayList<TextView>();

    private ArrayList<Message> messages;
    private ListView listView;
    public static ChatListAdapter adapter;
    public static ArrayList<Message> messageList;

    protected void onCreate (Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        Bundle b = getIntent().getExtras();

        roomId = b.getInt("roomId");
        this.roomName = b.getString("roomName");

        active = true;

        makeLayout(R.layout.activity_chat, roomName, R.menu.room);

        initView();

        initEventListener();
    }

    private void initView()
    {
        sendMessage = (ImageButton)findViewById(R.id.send_message);
        message = (TextView)findViewById(R.id.message);

        inputs.addAll(Arrays.asList(message));

        sendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ValidationFactory.validation(inputs, context)) {

                    Message message = fillValues();
                    sendMessage(message);
                }
            }
        });

        makeList();
    }

    private void makeList()
    {
        messageList = new ArrayList<Message>();

        UserInfo userInfo = CrypTalkerApplication.getUserInfo();
        messages = userInfo.getMessagesByRoomId(roomId);

        if (messages == null) {
            return;
        }

        listView = (ListView) findViewById(R.id.list);
        adapter = new ChatListAdapter(this, messageList, userInfo.getUser());
        listView.setAdapter(adapter);

        Log.i(TAG, "There is " + String.valueOf(messages.size()) + " messages !");

        for (Message message1 : messages) {
            messageList.add(message1);
        }

        adapter.notifyDataSetChanged();
    }

    private Message fillValues()
    {
        Message newMessage = new Message();
        newMessage.setRoom_id(roomId);
        newMessage.setMessage(message.getText().toString());
        newMessage.setFrom(CrypTalkerApplication.getUserInfo().getUser().getPseudo());

        return newMessage;
    }

    private void resetInput()
    {
        message.setText("");
    }

    private void sendMessage(final Message message) {

        // If the request fail the message is still displayed :(
        CrypTalkerApplication.getUserInfo().addMessageToRoom(roomId, message);
        makeList();
        resetInput();

        RequestManager.getInstance(ChatActivity.this).sendMessageRequest(message, new com.android.volley.Response.Listener<Response>() {

            @Override
            public void onResponse(Response response) {

                if (!response.isSuccess()) {

                    if (response.getErrors().length() > 0) {
                        ValidationFactory.parseJsonErrors(response.getErrors(), ChatActivity.this);
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error during the request => " + error.toString());
            }
        });
    }

    public void onEvent (MessageEvent event)
    {
        // Refresh the listView
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                makeList();
            }
        });
    }

    public void initEventListener()
    {
        EventBus.getDefault().register(this);
    }

    public void destroyEventListener() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        active = true;
    }

    @Override
    public void onStop()
    {
        active = false;

        super.onDestroy();
    }

    @Override
    public void onDestroy()
    {
        active = false;
        destroyEventListener();

        super.onDestroy();
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        ChatActivity.active = active;
    }

    public static int getRoomId() {
        return roomId;
    }

    public static void setRoomId(int roomId) {
        ChatActivity.roomId = roomId;
    }
}

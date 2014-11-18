package tk.cryptalker.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.android.volley.VolleyError;
import tk.cryptalker.R;
import tk.cryptalker.factory.valdiation.ValidationFactory;
import tk.cryptalker.manager.RequestManager;
import tk.cryptalker.model.Message;
import tk.cryptalker.model.Response;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AbstractActivity
{
    private static final String TAG = "ChatActivity";
    private int roomId;
    private String roomName;

    ImageButton sendMessage;
    TextView message;
    private ArrayList<TextView> inputs = new ArrayList<TextView>();

    protected void onCreate (Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        Bundle b = getIntent().getExtras();

        this.roomId = b.getInt("roomId");
        this.roomName = b.getString("roomName");

        makeLayout(R.layout.activity_chat, roomName, R.menu.room);

        initView();
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
    }

    private void messageIsSend()
    {
        message.setText("");
        getWindow().getDecorView().clearFocus();
    }

    private Message fillValues()
    {
        Message newMessage = new Message();
        newMessage.setRoom_id(roomId);
        newMessage.setMessage(message.getText().toString());

        return newMessage;
    }

    private void sendMessage(final Message message) {

        RequestManager.getInstance(ChatActivity.this).sendMessageRequest(message, new com.android.volley.Response.Listener<Response>() {

            @Override
            public void onResponse(Response response) {

                if (response.isSuccess()) {

                    messageIsSend();
                } else {

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
}
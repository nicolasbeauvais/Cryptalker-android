package tk.cryptalker.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import tk.cryptalker.R;
import tk.cryptalker.model.Message;
import tk.cryptalker.model.User;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    private static final String TAG = "ChatListAdapter";

    private Activity activity;
    private LayoutInflater inflater;
    private List<Message> messages;
    private User owner;

    public ChatListAdapter(Activity activity, List<Message> messages, User owner) {
        this.activity = activity;
        this.messages = messages;
        this.owner = owner;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int location) {
        return messages.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // getting room data for the row
        final Message message = messages.get(position);

        if (message.getMessage() == null) {

            return null;
        } else {

            if(message.getFrom().equals(owner.getPseudo())) {
                convertView = inflater.inflate(R.layout.row_chat_owner, null);
            } else {
                convertView = inflater.inflate(R.layout.row_chat_other, null);
            }

            TextView messageElement = (TextView) convertView.findViewById(R.id.message);

            messageElement.setText(message.getMessage());

            Log.i(TAG, message.toString());

            if (message.isFail()) {
                TextView fail = (TextView) convertView.findViewById(R.id.fail);
                fail.setVisibility(View.VISIBLE);
            } else if (message.isPending()) {

                TextView pending = (TextView) convertView.findViewById(R.id.pending);
                pending.setVisibility(View.VISIBLE);
            }

            //Log.i(TAG, message.toString());

            return convertView;
        }
    }
}

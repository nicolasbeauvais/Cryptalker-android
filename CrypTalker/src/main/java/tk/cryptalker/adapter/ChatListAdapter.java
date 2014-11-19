package tk.cryptalker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import tk.cryptalker.R;
import tk.cryptalker.model.Message;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    private static final String TAG = "ChatListAdapter";

    private Activity activity;
    private LayoutInflater inflater;
    private List<Message> messages;

    public ChatListAdapter(Activity activity, List<Message> messages) {
        this.activity = activity;
        this.messages = messages;
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

            convertView = inflater.inflate(R.layout.row_chat, null);
            TextView messageElement = (TextView) convertView.findViewById(R.id.message);

            messageElement.setText(message.getMessage());

            return convertView;
        }
    }
}

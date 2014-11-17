package tk.cryptalker.activity;

import android.os.Bundle;
import tk.cryptalker.R;

public class ChatActivity extends AbstractActivity
{
    private static final String TAG = "ChatActivity";


    @Override
    protected void onCreate (Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        makeLayout(R.layout.activity_create_account, R.string.create_account_header_title, R.menu.empty);
    }
}

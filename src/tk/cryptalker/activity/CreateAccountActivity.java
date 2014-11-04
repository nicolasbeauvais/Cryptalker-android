package tk.cryptalker.activity;

import android.os.Bundle;
import tk.cryptalker.R;

public class CreateAccountActivity extends AbstractActivity
{

    @Override
    protected void onCreate (Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        context = getApplicationContext();
        
        makeLayout(R.layout.activity_create_account, R.string.create_account_header_title);

        attachPageChange(R.id.go_login, LoginActivity.class);
    }
}

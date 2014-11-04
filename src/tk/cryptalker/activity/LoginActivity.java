package tk.cryptalker.activity;

import android.os.Bundle;
import tk.cryptalker.R;

public class LoginActivity extends AbstractActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        makeLayout(R.layout.activity_login, R.string.login_header_title);

        attachPageChange(R.id.go_register, CreateAccountActivity.class);
    }
}

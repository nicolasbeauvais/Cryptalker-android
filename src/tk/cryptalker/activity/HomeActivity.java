package tk.cryptalker.activity;

import android.os.Bundle;
import tk.cryptalker.R;
import tk.cryptalker.application.CrypTalkerApplication;

public class HomeActivity extends AbstractActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        makeLayout(R.layout.activity_home, R.string.app_name);

        attachPageChange(R.id.go_register, CreateAccountActivity.class);
        attachPageChange(R.id.go_login, LoginActivity.class);
    }
}

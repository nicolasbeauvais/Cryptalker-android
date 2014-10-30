package tk.cryptalker;

import android.os.Bundle;

public class HomeActivity extends AbstractActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        makeLayout(R.layout.activity_home, R.string.app_name);

        attachPageChange(R.id.go_register, CreateAccountActivity.class);
        attachPageChange(R.id.go_login, LoginActivity.class);
    }
}

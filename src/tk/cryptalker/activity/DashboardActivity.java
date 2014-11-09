package tk.cryptalker.activity;


import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import tk.cryptalker.R;
import tk.cryptalker.dialog.AddFriendDialogFragment;

public class DashboardActivity extends AbstractActivity {

    private MenuItem menuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeLayout(R.layout.activity_dashboard, R.string.dashboard_title, R.menu.dashboard);
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

                        AddFriendDialogFragment newFragment = new AddFriendDialogFragment();
                        newFragment.show(getFragmentManager(), "AddFriendDialogFragment");

                        return true;
                    }
                });
            }
        });

        return value;
    }

}

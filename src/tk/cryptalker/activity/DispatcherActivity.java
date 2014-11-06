package tk.cryptalker.activity;

import android.os.Bundle;
import tk.cryptalker.R;


public class DispatcherActivity extends AbstractActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        makeLayout(R.layout.activity_dispatcher, R.string.app_name);
    }
}

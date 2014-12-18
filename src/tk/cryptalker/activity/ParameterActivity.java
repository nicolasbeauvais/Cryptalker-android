package tk.cryptalker.activity;

import android.os.Bundle;
import tk.cryptalker.R;

public class ParameterActivity extends AbstractActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        makeLayout(R.layout.activity_parameter, getString(R.string.parameter_header_title), R.menu.empty);
    }
}

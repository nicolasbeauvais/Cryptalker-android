package tk.cryptalker.activity;

import android.app.ProgressDialog;
import android.view.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.R;
import tk.cryptalker.application.CrypTalkerApplication;
import tk.cryptalker.factory.storage.StorageFactory;

public class AbstractActivity extends Activity
{
    private static final String TAG = "AbstractActivity";

    static Activity instance;
    static Context context;
    private int menu;
    private static ProgressDialog progress;

    public AbstractActivity()
    {
        AbstractActivity.instance = this;
    }

    public void makeLayout(int layout, String title, int menu)
    {
        context = getApplicationContext();
        setContentView(layout);

        setTitle(title);

        this.menu = menu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(this.menu, menu);

        getActionBar().setIcon(R.drawable.ic_drawer);

        return super.onCreateOptionsMenu(menu);
    }

    public void attachPageChange(int element, Class destination)
    {
        Button button = (Button)findViewById(element);
        button.setOnClickListener(pageChange(button, context, destination));
    }

    private OnClickListener pageChange(final Button button, final Context context, final Class destination)
    {
        return new OnClickListener() {

            public void onClick(View view) {
                if (view == button) {
                    Intent intent = new Intent(context, destination);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        };
    }

    public static Context getContext()
    {
        return context;
    }

    public static void setContext(Context context) {
        AbstractActivity.context = context;
    }

    public static void showProgress(String dialogTitle, String dialogMessage)
    {
        progress = ProgressDialog.show(instance, dialogTitle, dialogMessage, true);

    }

    public static void hideProgress()
    {
        progress.dismiss();
    }
}

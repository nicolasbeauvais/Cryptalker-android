package tk.cryptalker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AbstractActivity extends Activity
{
    Context context;

    public void makeLayout(int layout, int title)
    {
        setContentView(layout);

        TextView headerTitle = (TextView)findViewById(R.id._header_title);
        headerTitle.setText(title);
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
}

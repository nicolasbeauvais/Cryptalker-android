package tk.cryptalker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import tk.cryptalker.R;

public class AbstractActivity extends Activity
{
    static Context context;

    public void makeLayout(int layout, int title)
    {
        context = getApplicationContext();
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

    public static Context getContext() {
        return context;
    }

    public boolean validation(TextView[] textViews)
    {
        // Reset validation
        Boolean validated = true;

        for (final TextView textView : textViews) {

            // Reset Errors
            textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (textView.getText().length() > 0) {
                        textView.setError(null);
                    }
                }
            });


            // Check
            String content = textView.getText().toString();
            if (content == null || content.length() == 0) {
                textView.setError(Html.fromHtml("<font color='red'>"
                        + getResources().getString(R.string.validation_input_required) + "</font>"));
                validated = false;
            }
        }

        return validated;
    }
}

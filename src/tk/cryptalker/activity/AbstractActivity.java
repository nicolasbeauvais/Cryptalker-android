package tk.cryptalker.activity;

import tk.cryptalker.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

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

    public static Context getContext()
    {
        return context;
    }

    public boolean validation(ArrayList<TextView> textViews)
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
                textView.setError(errorMessage(R.string.validation_input_required));
                validated = false;
            }
        }

        return validated;
    }

    public Spanned errorMessage(int reference)
    {
        return Html.fromHtml("<font color='red'>"
                + getResources().getString(reference) + "</font>");
    }

    public Spanned errorMessage(String string)
    {
        return Html.fromHtml("<font color='red'>" + string + "</font>");
    }

    public void parseJsonErrors(JSONObject errors)
    {
        JSONObject error_messages;

        try {
            error_messages = errors.getJSONObject("messages");

            if (error_messages.length() < 0) {
                return;
            }

            // Iterate errors from JSONObject
            Iterator iterator = error_messages.keys();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                JSONObject message = error_messages.getJSONObject(key);
                String value = message.getString("message");

                TextView textView = (TextView)findViewById(getResources().getIdentifier(key, "id", getPackageName()));
                textView.setError(errorMessage(value));
                textView.requestFocus();
            }

            Log.i("AbstractActivity@parseJsonErrors", error_messages.toString());
        } catch (JSONException e) {
            Log.i("AbstractActivity@parseJsonErrors", e.toString());
        }
    }
}

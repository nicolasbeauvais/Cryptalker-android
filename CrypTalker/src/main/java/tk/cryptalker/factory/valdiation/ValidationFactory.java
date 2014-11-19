package tk.cryptalker.factory.valdiation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import tk.cryptalker.R;

import java.util.ArrayList;
import java.util.Iterator;

public class ValidationFactory
{
    private static final String TAG = "ValidationFactory";

    public static boolean validation(ArrayList<TextView> textViews, Context context)
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
            if (content.length() == 0) {
                textView.setError(errorMessage(R.string.validation_input_required, context));
                validated = false;
            }
        }

        return validated;
    }

    public static SpannableStringBuilder errorMessage(int reference, Context context)
    {
        return errorMessage(context.getString(reference));
    }

    public static SpannableStringBuilder errorMessage(String string)
    {
        int color = Color.RED;
        ForegroundColorSpan fgcSpan = new ForegroundColorSpan(color);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(string);
        ssBuilder.setSpan(fgcSpan, 0, string.length(), 0);

        return ssBuilder;
    }

    public static void parseJsonErrors(JSONObject errors, Activity activity)
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

                int item = activity.getResources().getIdentifier(key, "id", activity.getPackageName());
                TextView textView = (TextView)activity.findViewById(item);
                textView.setError(errorMessage(value));
                textView.requestFocus();
            }

            Log.i("AbstractActivity@parseJsonErrors", error_messages.toString());
        } catch (JSONException e) {
            Log.i("AbstractActivity@parseJsonErrors", e.toString());
        }
    }

    public static void parseJsonErrorsDialog(JSONObject errors, AlertDialog dialog, Activity activity)
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

                int item = activity.getResources().getIdentifier(key, "id", activity.getPackageName());
                TextView textView = (TextView)dialog.findViewById(item);
                textView.setError(errorMessage(value));
                textView.requestFocus();
            }

            Log.i("AbstractActivity@parseJsonErrors", error_messages.toString());
        } catch (JSONException e) {
            Log.i("AbstractActivity@parseJsonErrors", e.toString());
        }
    }
}

package tk.cryptalker.activity;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import tk.cryptalker.model.User;

import java.util.ArrayList;
import java.util.Iterator;

public class AbstractActivity extends Activity
{
    static Context context;

    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_TOKEN = "token";
    private static final String PROPERTY_APP_VERSION = "appVersion";

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

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    public void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences();
        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    public SharedPreferences getGcmPreferences() {

        return getSharedPreferences(AbstractActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void storeToken(String token) {
        final SharedPreferences prefs = getGcmPreferences();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        final SharedPreferences prefs = getGcmPreferences();
        String token = prefs.getString(PROPERTY_TOKEN, "");

        return token;
    }
}

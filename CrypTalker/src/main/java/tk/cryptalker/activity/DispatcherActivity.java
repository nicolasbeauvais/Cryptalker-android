package tk.cryptalker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.json.JSONException;
import tk.cryptalker.R;
import tk.cryptalker.application.CrypTalkerApplication;
import tk.cryptalker.factory.storage.StorageFactory;
import tk.cryptalker.manager.RequestManager;
import tk.cryptalker.model.Response;
import tk.cryptalker.model.User;

import java.io.IOException;

public class DispatcherActivity extends AbstractActivity
{
    private static final String TAG = "DispatcherActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    Button refresh;
    ProgressBar progressBar;
    TextView loadingMsg;

    String SENDER_ID = "596702755873";
    GoogleCloudMessaging gcm;
    Context context;
    String regId;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContext(context);

        makeLayout(R.layout.activity_dispatcher, getString(R.string.app_name), R.menu.empty);

        context = getApplicationContext();

        initView();

        // Check network connection
        if (!CrypTalkerApplication.isNetworkAvailable(context)) {

            // Hide progressBar
            progressBar.setVisibility(View.INVISIBLE);

            // Change loading text
            loadingMsg.setText(context.getText(R.string.no_internet_connection));

            // Show reload button
            refresh.setVisibility(View.VISIBLE);

            return;
        }

        dispatch();
    }

    private void initView()
    {
        refresh = (Button)findViewById(R.id.refresh);
        progressBar = (ProgressBar)findViewById(R.id.dispatcher_progressbar);
        loadingMsg = (TextView)findViewById(R.id.loading_msg);

        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    /**
     * Check device for Play Services APK. If check succeeds, proceed with GCM registration.
     */
    private void dispatch()
    {
        if (checkPlayServices()) {

            gcm = GoogleCloudMessaging.getInstance(this);
            regId = StorageFactory.getRegistrationId();

            if (regId.isEmpty()) {
                registerInBackground();
            } else {

                String token = StorageFactory.getToken();

                // If has token, attempt a login
                if (!token.isEmpty()) {

                    User user = fillValues();
                    loginWithTokenUser(user);
                } else {

                    Intent intent = new Intent(DispatcherActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

        } else {
            Toast.makeText(context, R.string.no_apk, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context, R.string.no_apk, Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String msg = "";

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;

                    StorageFactory.storeRegistrationId(regId);

                    Intent intent = new Intent(DispatcherActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }

                return msg;
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private User fillValues()
    {
        User user =  new User();
        user.setMobileId(StorageFactory.getRegistrationId());
        user.setToken(StorageFactory.getToken());

        return user;
    }

    private void loginWithTokenUser(final User user){

        RequestManager.getInstance(DispatcherActivity.this).loginWithTokenUser(user, new Listener<Response>() {

            @Override
            public void onResponse(Response response) {

                if (response.isSuccess()) {

                    try {
                        StorageFactory.storeToken(response.getData().getString("token"));

                        StorageFactory.getUserInfo();

                    } catch (JSONException e) {
                        Log.i(TAG, "JSON Exception on user loginWithTokenUser return parsing");
                    }
                } else {

                    Log.i(TAG, "response fail :(");
                    Log.i(TAG, response.toString());

                    Intent intent = new Intent(DispatcherActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error during the request => " + error.toString());
            }
        });
    }

}

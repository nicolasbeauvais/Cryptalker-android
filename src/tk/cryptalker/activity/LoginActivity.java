package tk.cryptalker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

import org.json.JSONException;

import tk.cryptalker.R;
import tk.cryptalker.manager.RequestManager;
import tk.cryptalker.model.Response;
import tk.cryptalker.model.User;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AbstractActivity
{
    private static final String TAG = "LoginActivity";

    private EditText userLogin;
    private EditText userPassword;

    private ArrayList<TextView> inputs = new ArrayList<TextView>();

    private Button loginSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        makeLayout(R.layout.activity_login, R.string.login_header_title, R.menu.empty);

        attachPageChange(R.id.go_register, CreateAccountActivity.class);

        initViews();
    }

    private void initViews()
    {
        // Initialise inputs
        userLogin = (EditText)findViewById(R.id.login);
        userPassword = (EditText)findViewById(R.id.password);

        // Set Inputs
        inputs.addAll(Arrays.asList(userLogin, userPassword));

        // Form submit action listener
        loginSubmit = (Button) findViewById(R.id.login_submit);
        loginSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (validation(inputs)){

                    User user = fillValues();
                    loginUser(user);
                }
            }
        });
    }

    private User fillValues()
    {
        User user = new User();
        user.setLogin(userLogin.getText().toString());
        user.setPassword(userPassword.getText().toString());
        user.setMobileId(getRegistrationId(context));

        return user;
    }

    private void loginUser(final User user){
        try {
            RequestManager.getInstance(LoginActivity.this).loginUser(user, new com.android.volley.Response.Listener<Response>() {

                @Override
                public void onResponse(Response response) {

                    if (response.isSuccess()) {

                        try {
                            storeToken(response.getData().getString("token"));

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        } catch (JSONException e) {
                            Log.i(TAG, "JSON Exception on user login return parsing");
                        }
                    } else {

                        if (response.getErrors().length() > 0) {
                            parseJsonErrors(response.getErrors());
                        }
                    }
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "Error during the request => " + error.toString());
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "Error executing request " + e.getMessage(), e);
        }
    }
}

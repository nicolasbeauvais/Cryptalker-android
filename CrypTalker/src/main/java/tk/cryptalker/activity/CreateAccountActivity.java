package tk.cryptalker.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONException;

import tk.cryptalker.R;
import tk.cryptalker.factory.valdiation.ValidationFactory;
import tk.cryptalker.manager.RequestManager;
import tk.cryptalker.model.Response;
import tk.cryptalker.model.User;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateAccountActivity extends AbstractActivity
{
    private static final String TAG = "CreateAccountActivity";

    private EditText userEmail;
    private EditText userPseudo;
    private EditText userPassword;
    private EditText userPasswordConfirmation;

    private ArrayList<TextView> inputs = new ArrayList<TextView>();

    private Button createAccountSubmit;

    @Override
    protected void onCreate (Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        makeLayout(R.layout.activity_create_account, R.string.create_account_header_title, R.menu.empty);

        attachPageChange(R.id.go_login, LoginActivity.class);

        initViews();
    }

    private void initViews()
    {
        // Initialise inputs
        userEmail = (EditText)findViewById(R.id.email);
        userPseudo = (EditText)findViewById(R.id.pseudo);
        userPassword = (EditText)findViewById(R.id.password);
        userPasswordConfirmation = (EditText)findViewById(R.id.password_confirmation);

        // Set Inputs
        inputs.addAll(Arrays.asList(userEmail, userPseudo, userPassword, userPasswordConfirmation));

        // Form submit action listener
        createAccountSubmit = (Button) findViewById(R.id.create_account_submit);
        createAccountSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ValidationFactory.validation(inputs, context)){

                    User user = fillValues();
                    createUser(user);
                }
            }
        });
    }

    private User fillValues()
    {
        User user =  new User();
        user.setEmail(userEmail.getText().toString());
        user.setPseudo(userPseudo.getText().toString());
        user.setPassword(userPassword.getText().toString());
        user.setPasswordConfirmation(userPasswordConfirmation.getText().toString());
        user.setMobileId(getRegistrationId(context));

        return user;
    }

    private void createUser(final User user){

        RequestManager.getInstance(CreateAccountActivity.this).createUser(user, new Listener<Response>() {

            @Override
            public void onResponse(Response response) {

                if (response.isSuccess()) {

                    try {
                        storeToken(response.getData().getString("token"));

                        getUserInfo();

                    } catch (JSONException e) {
                        Log.i(TAG, "JSON Exception on user create return parsing");
                    }
                } else {

                    if (response.getErrors().length() > 0) {
                        ValidationFactory.parseJsonErrors(response.getErrors(), CreateAccountActivity.this);
                    }
                }
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error during the request => " + error.toString());
            }
        });
    }
}

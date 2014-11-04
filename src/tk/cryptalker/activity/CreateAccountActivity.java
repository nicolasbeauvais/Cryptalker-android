package tk.cryptalker.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONException;

import tk.cryptalker.R;
import tk.cryptalker.manager.RequestManager;
import tk.cryptalker.model.Response;
import tk.cryptalker.model.User;
import tk.cryptalker.util.CryptoUtils;

public class CreateAccountActivity extends AbstractActivity
{
    private static final String TAG = "CreateAccountActivity";

    private EditText userEmail;
    private EditText userPseudo;
    private EditText userPassword;
    private EditText userPasswordConfirmation;

    private Button createAccountSubmit;

    @Override
    protected void onCreate (Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        makeLayout(R.layout.activity_create_account, R.string.create_account_header_title);

        attachPageChange(R.id.go_login, LoginActivity.class);

        initViews();
    }

    private void initViews()
    {
        userEmail = (EditText)findViewById(R.id.email);
        userPseudo = (EditText)findViewById(R.id.pseudo);
        userPassword = (EditText)findViewById(R.id.password);
        userPasswordConfirmation = (EditText)findViewById(R.id.password_confirmation);

        createAccountSubmit = (Button) findViewById(R.id.create_account_submit);

        createAccountSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (checkFields()){
                    User user = fillValues();
                    createUser(user);
                } else {
                    Log.i(TAG, "fail");
                }
            }
        });
    }

    private void createUser(final User user){
        try {
            RequestManager.getInstance(CreateAccountActivity.this).createUser(user, new Listener<Response>() {

                @Override
                public void onResponse(Response response) {
                    Log.i(TAG, "Response " + user.toString());
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

    protected User fillValues() {
        User user = new User();
        user.setEmail(userEmail.getText().toString());
        user.setPseudo(userPseudo.getText().toString());
        user.setPassword(CryptoUtils.getHash(userPassword.getText().toString()));

        return user;
    }

    protected boolean checkFields() {
        String email = userEmail.getText().toString();
        String pseudo = userPseudo.getText().toString();
        String password = userPassword.getText().toString();
        String passwordConfirmation = userPasswordConfirmation.getText().toString();

        if (email == null || email.length() == 0) {
            return false;
        }

        if (pseudo == null || pseudo.length() == 0) {
            return false;
        }

        if (password == null || password.length() == 0) {
            return false;
        }

        if (passwordConfirmation == null || passwordConfirmation.length() == 0 || !password.equals(passwordConfirmation)) {
            return false;
        }

        return true;
    }
}


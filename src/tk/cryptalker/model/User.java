package tk.cryptalker.model;

import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User
{

    private Long id;
    private String email;
    private String pseudo;
    private String password;
    private String password_confirmation;
    private String pseudoOrEmail;
    private String mobile_id;
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return password_confirmation;
    }

    public void setPasswordConfirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getMobileId() {
        return mobile_id;
    }

    public void setMobileId(String mobile_id) {
        this.mobile_id = mobile_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return pseudoOrEmail;
    }

    public void setLogin(String pseudoOrEmail) {
        this.pseudoOrEmail = pseudoOrEmail;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", password='" + password + '\'' +
                ", password_confirmation='" + password_confirmation + '\'' +
                ", pseudoOrEmail='" + pseudoOrEmail + '\'' +
                ", mobile_id='" + mobile_id + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public JSONObject toJSON(){

        JSONObject j= new JSONObject();
        try {
            j.accumulate("id", getId());
            j.accumulate("email", getEmail());
            j.accumulate("pseudo", getPseudo());
            j.accumulate("password", getPassword());
            j.accumulate("password_confirmation", getPasswordConfirmation());
            j.accumulate("pseudoOrEmail", getLogin());
            j.accumulate("mobile_id", getMobileId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j;
    }
}

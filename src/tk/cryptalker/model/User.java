package tk.cryptalker.model;

import android.widget.TextView;

import java.util.ArrayList;

public class User
{

    private Long id;
    private String email;
    private String pseudo;
    private String password;
    private String password_confirmation;

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

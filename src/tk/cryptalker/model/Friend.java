package tk.cryptalker.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Friend
{

    private Long id;
    private String pseudo;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPseudo()
    {
        return pseudo;
    }

    public void setPseudo(String pseudo)
    {
        this.pseudo = pseudo;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                '}';
    }

    public JSONObject toJSON()
    {

        JSONObject j= new JSONObject();
        try {
            j.accumulate("id", getId());
            j.accumulate("pseudo", getPseudo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j;
    }
}

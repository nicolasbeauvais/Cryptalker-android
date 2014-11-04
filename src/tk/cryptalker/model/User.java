package tk.cryptalker.model;

public class User
{

    private Long userID;

    private String userPseudo;

    private String userHash;

    private Long userCreationDate;

    public Long getUserID()
    {
        return userID;
    }

    public void setUserID(Long userID)
    {
        this.userID = userID;
    }

    public String getUserPseudo()
    {
        return userPseudo;
    }

    public void setUserPseudo(String userPseudo)
    {
        this.userPseudo = userPseudo;
    }

    public String getUserHash()
    {
        return userHash;
    }

    public void setUserHash(String userHash)
    {
        this.userHash = userHash;
    }

    public Long getUserCreationDate()
    {
        return userCreationDate;
    }

    public void setUserCreationDate(Long userCreationDate)
    {
        this.userCreationDate = userCreationDate;
    }

    @Override
    public String toString()
    {
        return "User [userID=" + userID + ", userPseudo=" + userPseudo
                + ", userHash=" + userHash + ", userCreationDate="
                + userCreationDate + "]";
    }

}

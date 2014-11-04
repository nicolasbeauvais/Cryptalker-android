package tk.cryptalker.model;

public class Token
{

    private String tokenValue;

    public String getTokenValue()
    {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue)
    {
        this.tokenValue = tokenValue;
    }

    @Override
    public String toString()
    {
        return "Token [tokenValue=" + tokenValue + "]";
    }

}

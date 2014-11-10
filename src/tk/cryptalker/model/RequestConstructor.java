package tk.cryptalker.model;

import com.android.volley.Response.Listener;

public class RequestConstructor {

    private int verb;
    private String rest;
    private Listener listener;

    public int getVerb()
    {
        return verb;
    }

    public void setVerb(int verb)
    {
        this.verb = verb;
    }

    public String getRest()
    {
        return rest;
    }

    public void setRest(String rest)
    {
        this.rest = rest;
    }

    public Listener getListener()
    {
        return listener;
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }
}

package tk.cryptalker.model;

import android.content.Context;
import com.android.volley.Response.Listener;
import tk.cryptalker.R;
import tk.cryptalker.activity.AbstractActivity;

public class RequestConstructor {

    private int verb;
    private String rest;
    private Listener listener;
    private boolean progressDialog;
    private String progressDialogTitle;
    private String progressDialogMessage;

    public RequestConstructor ()
    {
        Context context = AbstractActivity.getContext();

        this.progressDialog = true;
        this.progressDialogTitle = context.getResources().getString(R.string.dialog_progress_default_title);
        this.progressDialogMessage = context.getResources().getString(R.string.dialog_progress_default_message);
    }

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

    public boolean isProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(boolean progressDialog) {
        this.progressDialog = progressDialog;
    }

    public String getProgressDialogTitle() {
        return progressDialogTitle;
    }

    public void setProgressDialogTitle(String progressDialogTitle) {
        this.progressDialogTitle = progressDialogTitle;
    }

    public void setProgressDialogTitle(int id) {
        this.progressDialogTitle = AbstractActivity.getContext().getResources().getString(id);
    }

    public String getProgressDialogMessage() {
        return progressDialogMessage;
    }

    public void setProgressDialogMessage(String progressDialogMessage) {
        this.progressDialogMessage = progressDialogMessage;
    }

    public void setProgressDialogMessage(int id) {
        this.progressDialogMessage = AbstractActivity.getContext().getResources().getString(id);
    }
}

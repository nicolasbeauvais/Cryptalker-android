package tk.cryptalker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import com.android.volley.VolleyError;
import tk.cryptalker.R;
import tk.cryptalker.activity.DashboardActivity;
import tk.cryptalker.factory.valdiation.ValidationFactory;
import tk.cryptalker.manager.RequestManager;
import tk.cryptalker.model.Friend;
import tk.cryptalker.model.Response;

import java.util.ArrayList;
import java.util.Arrays;

public class AddFriendDialogFragment extends DialogFragment
{
    private static final String TAG = "AddFriendDialogFragment";

    private Activity activity;
    private AlertDialog dialog;
    private EditText pseudo;
    private ArrayList<TextView> inputs = new ArrayList<TextView>();

    public AddFriendDialogFragment(Activity activity)
    {
        this.activity = activity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View inflateView = inflater.inflate(R.layout.dialog_add_friend, null);

        builder.setView(inflateView);
        builder.setPositiveButton(R.string.dialog_add_friend_positive_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(R.string.dialog_add_friend_negative_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AddFriendDialogFragment.this.getDialog().cancel();
            }
        });

        dialog = builder.create();

        return dialog;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        AlertDialog d = (AlertDialog)getDialog();

        pseudo = (EditText)d.findViewById(R.id.pseudo);

        inputs.addAll(Arrays.asList(pseudo));

        Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (ValidationFactory.validation(inputs, getActivity().getBaseContext())) {
                    Friend friend = fillValues();
                    addFriend(friend);
                }
            }
        });
    }

    private Friend fillValues()
    {
        Friend friend = new Friend();
        friend.setPseudo(pseudo.getText().toString());

        return friend;
    }

    private void addFriend(final Friend friend){

        RequestManager.getInstance(getActivity()).addFriendRequest(friend, new com.android.volley.Response.Listener<Response>() {

            @Override
            public void onResponse(Response response) {

                if (response.isSuccess()) {
                    dismiss();
                    Toast.makeText(getActivity(), R.string.dialog_add_friend_success, Toast.LENGTH_LONG).show();
                } else {

                    if (response.getErrors().length() > 0) {
                        ValidationFactory.parseJsonErrorsDialog(response.getErrors(), dialog, activity);
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error during the request => " + error.toString());
            }
        });
    }
}

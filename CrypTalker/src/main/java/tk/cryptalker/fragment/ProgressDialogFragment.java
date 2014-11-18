package tk.cryptalker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import tk.cryptalker.R;

public class ProgressDialogFragment extends DialogFragment
{
    private static final String TAG = "ProgressDialogFragment";

    private Activity activity;
    private AlertDialog dialog;

    public ProgressDialogFragment()
    {
        this.activity = getActivity();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View inflateView = inflater.inflate(R.layout.dialog_progress_wheel, null);

        builder.setView(inflateView);
        dialog = builder.create();

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        return dialog;
    }
}

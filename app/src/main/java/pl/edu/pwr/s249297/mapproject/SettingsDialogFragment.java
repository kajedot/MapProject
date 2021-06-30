package pl.edu.pwr.s249297.mapproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class SettingsDialogFragment extends DialogFragment {
    private boolean saveFlag = false;
    private Activity activity;
    private String serverUrl;
    private Context context;
    private FragmentManager fragmentManager;


    public SettingsDialogFragment(Activity activity, Context context, FragmentManager fragmentManager){
        this.activity = activity;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.settings_dialog, null);
        builder.setTitle("Settings")
                .setView(v)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        saveFlag = true;
                        EditText et = (EditText) v.findViewById(R.id.server_url);
                        serverUrl = et.getText().toString();
                    }
                });
        return builder.create();
    }


    @Override
    public void onDetach() {

        super.onDetach();

        if (saveFlag){
            if (URLUtil.isValidUrl(serverUrl)) {
                writeToPreference(serverUrl);
                activity.recreate();
            } else {
                MessageDialogFragment dialog = new MessageDialogFragment("Error: url is invalid");
                dialog.show(fragmentManager, "responseDialog");
            }
        }

    }

    public void writeToPreference(String thePreference)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("pref",0).edit();
        ((SharedPreferences.Editor) editor).putString("server_ip", thePreference);
        editor.apply();
    }

}

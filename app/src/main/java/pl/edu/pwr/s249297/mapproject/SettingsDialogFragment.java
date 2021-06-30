package pl.edu.pwr.s249297.mapproject;

import android.annotation.SuppressLint;
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
    private SeekBar seekBar;
    private TextView refreshTextView;
    private int refreshInter;


    public SettingsDialogFragment(Activity activity, Context context, FragmentManager fragmentManager){
        this.activity = activity;
        this.context = context;
        this.fragmentManager = fragmentManager;

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreateDialog(savedInstanceState);
        getPreferences();

        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.settings_dialog, null);

        EditText et = (EditText) v.findViewById(R.id.server_url);
        et.setText(serverUrl);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Settings")
                .setView(v)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        saveFlag = true;

                        serverUrl = et.getText().toString();
                    }
                });

        refreshTextView = (TextView)v.findViewById(R.id.refreshTextView);
        refreshTextView.setText((refreshInter+5) + " s");

        seekBar=(SeekBar)v.findViewById(R.id.refreshSeekBar);
        seekBar.setProgress(refreshInter-5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refreshInter = progress + 5;
                refreshTextView.setText((progress + 5) + " s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
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

    private void getPreferences()
    {
        SharedPreferences sp = context.getSharedPreferences("pref",0);
        refreshInter = sp.getInt("refresh_int",5);
        serverUrl = sp.getString("server_ip","http://192.168.1.41:8888/");

    }

    public void writeToPreference(String thePreference)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("pref",0).edit();
        ((SharedPreferences.Editor) editor).putString("server_ip", thePreference);
        ((SharedPreferences.Editor) editor).putInt("refresh_int", refreshInter);
        editor.apply();
    }

}

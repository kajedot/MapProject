package pl.edu.pwr.s249297.mapproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class RoversDialogFragment extends DialogFragment {
    List<Rover> roversList;

    public RoversDialogFragment(List<Rover> roversList){
        this.roversList = roversList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog constructionlop;

        String[] roversStr;
        roversStr = roversToString(roversList);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Rovers");
        builder.setItems(roversStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }

    private String[] roversToString(List<Rover> rovers){
        String[] roversStr;
        roversStr = new String[rovers.size()];

        int tabIdx = 0;
        for (Rover rover : rovers) {
            StringBuilder content = new StringBuilder();
            content.append("\nID: ").append(rover.getRoverId()).append("\n");
            content.append("cord X: ").append(rover.getCordX()).append("\n");
            content.append("cord Y: ").append(rover.getCordY()).append("\n");
            content.append("angle: ").append(rover.getAngle()).append("\n");
            roversStr[tabIdx] = content.toString();
            tabIdx++;
        }
        return roversStr;
    }

}

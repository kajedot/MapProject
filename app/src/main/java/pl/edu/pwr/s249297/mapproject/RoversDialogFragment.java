package pl.edu.pwr.s249297.mapproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RoversDialogFragment extends DialogFragment {
    List<Rover> roversList;
    GoogleMap mMap;

    public RoversDialogFragment(List<Rover> roversList, GoogleMap mMap){
        this.roversList = roversList;
        this.mMap = mMap;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog constructionlop;

        if (roversList.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("No rovers available");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });

            return builder.create();

        }else {

            String[] roversStr;
            roversStr = roversToString(roversList);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Rovers");
            builder.setItems(roversStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LatLng roverPos = new LatLng(roversList.get(which).getCordX(), roversList.get(which).getCordY());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(roverPos));
                }
            });

            return builder.create();
        }
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

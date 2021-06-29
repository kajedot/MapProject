package pl.edu.pwr.s249297.mapproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RestAdapter restAdapter;
    private List<Rover> previousRovers = new ArrayList<>();
    private Map<Integer, Marker> markersMap = new TreeMap<>();

    private int mInterval = 5000;
    private Handler mHandler;

    private static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        restAdapter = new RestAdapter(getSupportFragmentManager());

        mHandler = new Handler();
        startRepeatingTask();

        FloatingActionButton fab_settings = findViewById(R.id.fab_settings);
        fab_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab_info = findViewById(R.id.fab_info);
        fab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoversDialogFragment dialog = new RoversDialogFragment(restAdapter.getRovers(), mMap);
                dialog.show(getSupportFragmentManager(), "roversDialog");
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //TODO mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void startRepeatingTask(){
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                fetchAndPin(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    private void fetchAndPin(){

        List<Rover> rovers = new ArrayList<>();

        restAdapter.callApi();

        rovers = restAdapter.getRovers();

        pinRovers(rovers);

////        Log.v(TAG, "previous rovers size: " + previousRovers.toString());
////        Log.v(TAG, "new rovers: " + rovers.toString());
//
//        if (! previousRovers.equals(rovers)){ //if rovers list changed from previous update
//            //pinRovers(rovers);
//            Log.v(TAG, "Rovers list changed");
//        } else {
//            Log.v(TAG, "Rovers list not changed");
//        }
//
//        previousRovers = rovers;


    }

    private void pinRovers(List<Rover> rovers){

        if (rovers.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No rovers available to show on the map", Toast.LENGTH_LONG);
            toast.show();
        } else {
            for (Rover rover : rovers) {
                LatLng roverPos = new LatLng(rover.getCordX(), rover.getCordY());
                if (! markersMap.containsKey(rover.getRoverId())) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(roverPos).title(
                            "Rover ID: " + rover.getRoverId() + " Heading: " + rover.getAngle()));

                    markersMap.put(rover.getRoverId(), marker);
                } else {
                    Objects.requireNonNull(markersMap.get(rover.getRoverId())).setPosition(roverPos);
                    Objects.requireNonNull(markersMap.get(rover.getRoverId())).setTitle(
                            "Rover ID: " + rover.getRoverId() + " Heading: " + rover.getAngle());
                }
            }

//            for (Map.Entry<Integer, Marker> entry : markersMap.entrySet()){
//                if (rovers.contains())
//            }
        }
    }
}
package pl.edu.pwr.s249297.mapproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RestAdapter restAdapter;
    private final Map<Integer, Marker> markersMap = new TreeMap<>();

    private int mInterval = 5000;
    private Handler mHandler;

    SettingsDialogFragment settingsDialog;
    RoversDialogFragment roversDialog;

    private static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        restAdapter = new RestAdapter(getSupportFragmentManager(), getApplicationContext());

        mHandler = new Handler();
        startRepeatingTask();

        settingsDialog = new SettingsDialogFragment(this);

        FloatingActionButton fab_settings = findViewById(R.id.fab_settings);
        fab_settings.setOnClickListener(view -> {

            settingsDialog.show(getSupportFragmentManager(), "settingsDialog");


        });

        FloatingActionButton fab_info = findViewById(R.id.fab_info);
        fab_info.setOnClickListener(view -> {
            roversDialog = new RoversDialogFragment(restAdapter.getRovers(), mMap);
            roversDialog.show(getSupportFragmentManager(), "roversDialog");
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopRepeatingTask();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
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
                fetchAndPin();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    private void fetchAndPin(){
        List<Rover> rovers = new ArrayList<>();

        restAdapter.callApi();
        rovers = restAdapter.getRovers();
        pinRovers(rovers);
    }

    private void pinRovers(List<Rover> rovers){

        if (! rovers.isEmpty()){
            for (Rover rover : rovers) {
                LatLng roverPos = new LatLng(rover.getCordX(), rover.getCordY());
                if (! markersMap.containsKey(rover.getRoverId())) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(roverPos).title(
                            "Rover ID: " + rover.getRoverId() + "  Heading: " + rover.getAngle()));

                    markersMap.put(rover.getRoverId(), marker);
                } else {
                    Objects.requireNonNull(markersMap.get(rover.getRoverId())).setPosition(roverPos);
                    Objects.requireNonNull(markersMap.get(rover.getRoverId())).setTitle(
                            "Rover ID: " + rover.getRoverId() + "  Heading: " + rover.getAngle());
                }
            }
        }
    }
}
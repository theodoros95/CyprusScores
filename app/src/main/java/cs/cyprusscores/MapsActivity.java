package cs.cyprusscores;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at GSP Stadium
        LatLng gsp = new LatLng(35.115045, 33.362724);
        mMap.addMarker(new MarkerOptions().position(gsp).title("GSP Stadium"));

        // Add a marker at Elias Poullos
        LatLng eliasP = new LatLng(35.108501, 33.407075);
        mMap.addMarker(new MarkerOptions().position(eliasP).title("Elias Poullos"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gsp, 12));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}

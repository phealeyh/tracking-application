package tracking.id11723222.com.trackingapplication.MapActivities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tracking.id11723222.com.trackingapplication.AboutPageActivity;
import tracking.id11723222.com.trackingapplication.Constants;
import tracking.id11723222.com.trackingapplication.CreateTimetableActivity;
import tracking.id11723222.com.trackingapplication.MainActivity;
import tracking.id11723222.com.trackingapplication.R;
import tracking.id11723222.com.trackingapplication.TrackingPreferences;
import tracking.id11723222.com.trackingapplication.settings.LocationSettingsChecker;

public class GoToLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    /**
     *Create the map fragment and use it within
     * the activity_go_to_location xml
     *@param savedInstanceState
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_go_to_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Will inflate the menu based on the menu_main menu
     *@param menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }





    /**
     * This will get a list of available coordinates based on the given
     * location interpreted in a postal address format, once these addresses are found by calling
     * a helper method getLocationsFromSelectedItem, the latitude and longitude coordinates
     * associated with that location are given as arguments for a new LatLng object, which will be
     * used when moving the googleMap object's camera. It will also show a marker showing the reason
     * associated with that reminder.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap){
        List<Address> addresses;
        Address address;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = getLocationsFromSelectedItem();
        mMap = googleMap;
        LatLng location = new LatLng(addresses.get(Constants.INITIAL_LOCATION).getLatitude(), addresses.get(Constants.INITIAL_LOCATION).getLongitude());
        mMap.addMarker(new MarkerOptions().position(location).title(getIntent().getStringExtra(Constants.ENTRY_REASON)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.ZOOM_LEVEL));
    }

    /**
     * This will get the location name as a string and find
     * the coordinates associated with that given address.
     * It will then return an address list associated with that location
     *@return List<Address>
     */

    private List<Address> getLocationsFromSelectedItem(){
        List<Address> tempAddress = null;
        try {
            tempAddress =  geocoder.getFromLocationName(getIntent().getStringExtra(Constants.EXTRA_LOCATION),
                    Constants.MAX_ADDRESS_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempAddress;
    }
}

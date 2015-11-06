package tracking.id11723222.com.trackingapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.Menu;
import android.view.MenuItem;

public class ShowUserLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager mManager;
    private LocationListener mListener;
    private Context mContext;

    /**
     * This will initialise the activity members and will setup the fragment to show a
     * google map. The location manager and listener are both used to listen to the position of
     * the user geographically.
     *@param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setLocationListener();
        mContext = getApplicationContext();
        mManager = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);
        mManager.requestLocationUpdates(mManager.NETWORK_PROVIDER,60000,1000,mListener);
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
     * Sets the action_settings element in the menu to listen for clicks.
     * It will start the CreateTimetableActivity class used for creating new entries based
     * on a blank intent.
     *@param item
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home_settings) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, CreateTimetableActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.tracking_preferences){
            startActivity(new Intent(this,TrackingPreferences.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setLocationListener(){
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }

    /**
     * This will basically pan the google map camera to the position provided by the network provider
     * obtained by the LocationManager. It will drop a pin at this location showing the user's
     * current position. If the marker is clicked, then the Latitude and Longitude values will
     * be shown.
     *@param googleMap
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //get longitude and latitude of current location
        Location lastLocation = mManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        LatLng currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLocation).title(currentLocation.toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, Constants.ZOOM_LEVEL));
    }
}

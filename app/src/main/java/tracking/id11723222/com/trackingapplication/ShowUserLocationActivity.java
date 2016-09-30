package tracking.id11723222.com.trackingapplication;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.util.Log;

import java.util.Locale;

public class ShowUserLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager mManager;
    private LocationListener mListener;
    private boolean latShowing;

    /**
     * This will initialise the activity members and will setup the fragment to show a
     * google map. The location manager and listener are both used to listen to the position of
     * the user geographically.
     *@param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_show_user_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setLocationListener();
        Context mContext = getApplicationContext();
        mManager = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);
        mManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1000, mListener);
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
     * @param googleMap
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location lastLocation = mManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        lastLocation.getLatitude();
        lastLocation.getLongitude();
        final LatLng currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        //display marker with the lat/lng location
        final Marker mark = mMap.addMarker(new MarkerOptions().position(currentLocation).title(currentLocation.toString()));
        latShowing = true;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, Constants.ZOOM_LEVEL));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(latShowing){
                    mark.setTitle(getPostalAddress(currentLocation));
                    latShowing = false;
                }
                else{
                    mark.setTitle(currentLocation.toString());
                    latShowing = true;
                }
                return false;
            }
        });
    }

    private String getPostalAddress(LatLng location){
        String address = "";
        try{
            Address add = new Geocoder(this, Locale.getDefault()).getFromLocation(location.latitude,location.longitude,1).get(0);
            address = add.getFeatureName() +  " " + add.getThoroughfare() + ", "
                    + add.getLocality() + ", " + add.getAdminArea() + ", " + add.getCountryName();
        } catch(Exception e){
            Log.e(Constants.ERROR, Constants.ADD_CLICKED_EXCEPTION, e);
        }
        return address;
    }
}

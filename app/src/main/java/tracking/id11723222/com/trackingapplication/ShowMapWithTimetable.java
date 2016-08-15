package tracking.id11723222.com.trackingapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderApi;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tracking.id11723222.com.trackingapplication.model.ReminderData;
import tracking.id11723222.com.trackingapplication.model.ReminderDatabaseHelper;
import tracking.id11723222.com.trackingapplication.services.LocationRetriever;

public class ShowMapWithTimetable extends FragmentActivity implements OnMapReadyCallback {

    private ListView mReminderListView;
    private GoogleMap mMap;
    private Context mContext;
    private Geocoder geocoder;
    private int currentPosition;
    private ReminderAdapter mReminderAdapter;


    /**
     * This will initialise the member ListView and will also set the class member's adapter
     * to a new instance of the ReminderAdapter helper inner class.
     * It will also set up the map fragment so that a map will be shown on most of the screen.
     * The list will be located beneath the map fragment.
     *@param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map_with_timetable);
        mContext = this;
        Intent intent = getIntent();
        String destination = intent.getStringExtra(Constants.EXTRA_LOCATION);
        // The adapter takes care of everything
        mReminderListView = (ListView) findViewById(R.id.reminder_list);
        mReminderListView.setAdapter(mReminderAdapter = new ReminderAdapter(this, destination));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_list);
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

        return super.onOptionsItemSelected(item);
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
    public void onMapReady(GoogleMap googleMap) {
        try {
            ReminderData tempReminder = ReminderDatabaseHelper.get(this).getReminderDataList(null).get(Constants.INITIAL_LOCATION);
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = getLocationFromSelectedItem(Constants.INITIAL_LOCATION);
            mMap = googleMap;
            LatLng location = new LatLng(addresses.get(Constants.ZERO).getLatitude(), addresses.get(Constants.ZERO).getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(tempReminder.getReason()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.ZOOM_LEVEL));
        } catch(Exception e) {
            Log.e(Constants.BAD_LIST,Constants.BAD_LIST);
            Toast.makeText(getApplicationContext(), Constants.EMPTY_LIST, Toast.LENGTH_LONG).show();
        }
    }



    /**
     * This will get the location name as a string and find
     * the coordinates associated with that given address.
     * It will then return an address list associated with that location
     * @return List<Address>
     */


    private List<Address> getLocationFromSelectedItem(int position){
        try {
            ReminderData tempReminder = ReminderDatabaseHelper.get(this).getReminderDataList(null).get(position);
            //problem line
            return  geocoder.getFromLocationName(tempReminder.getLocation(), Constants.ONE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    private class ReminderAdapter extends CursorAdapter {
        private String mLocation;
        private List<ReminderData> reminderList;

        /**
         * Creates a ReminderAdapter providing a list of reminder potentially filtered by location.
         *
         * @param context
         * @param location the location to filter by, or null if all reminders should be included.
         */
        public ReminderAdapter(Context context, String location) {
            super(context, ReminderDatabaseHelper.get(ShowMapWithTimetable.this).getReminderData(location), Constants.ZERO);
            mLocation = location;
            reminderList  = ReminderDatabaseHelper.get(ShowMapWithTimetable.this).getReminderDataList(null);
            currentPosition = Constants.ZERO;
        }

        /**
         * This will inflate the layout to the iterm reminder_single layout
         *
         *@param context
         *@param cursor
         *@param parent
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_single, null);
        }

        /**
         * Sets the occupying data from the cursor database query results and then
         * sets it's correlating columns to the correct text views
         *@param view
         *@param context
         *@param cursor
         */

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // This is made "final" so that it can be referenced from within the anonymous OnClickListener below.
            final ReminderData reminderData = ((ReminderDatabaseHelper.ReminderCursor) cursor).getReminderData();

            TextView locationTV = (TextView) view.findViewById(R.id.list_item_location_textview);
            TextView dateTV = (TextView) view.findViewById(R.id.list_item_date_textview);
            TextView timeTV = (TextView) view.findViewById(R.id.list_item_time_textview);
            TextView reasonTV = (TextView) view.findViewById(R.id.list_item_reason_textview);
            locationTV.setText(reminderData.getLocation());
            dateTV.setText(reminderData.getDate());
            timeTV.setText(reminderData.getTime());
            reasonTV.setText(reminderData.getReason());
            setNextButton(view);
            setBackButton(view);


        }
        /**
         * This will basically iterate over the database reminder table by iterating over the
         * list array. Range checking will be done by creating a new range class that has the starting
         * position of the list, the last element of the list, and the counter variable which is the current
         * position the list is within. The range checking is used to see whether or not the next
         * position exists, if it does, retrieve it and then get it's location from a geocolder helper class
         * and then get it's latitude and longitude locations from that and move the GoogleMap camera
         * to that specific location.
         *@param view
         */

        private void setNextButton(View view){
            ImageButton nextButton = (ImageButton) view.findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tempCounter = currentPosition;
                    if(new Range<Integer>(Constants.ZERO, (reminderList.size() - Constants.ONE)).contains(++tempCounter)){
                        //increment counter position by 1
                        ++currentPosition;
                    }
                    ReminderData temp = reminderList.get(currentPosition);
                    changeCursor(ReminderDatabaseHelper.get(ShowMapWithTimetable.this).getReminderData(temp.getLocation()));
                    List<Address> addresses = getLocationFromSelectedItem(currentPosition);
                    LatLng location = new LatLng(addresses.get(Constants.INITIAL_LOCATION).getLatitude(),
                            addresses.get(Constants.INITIAL_LOCATION).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(location).title(temp.getReason()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.ZOOM_LEVEL));

                }
            });

        }
        /**
         * This will basically iterate over the database reminder table by iterating over the
         * list array. Range checking will be done by creating a new range class that has the starting
         * position of the list, the last element of the list, and the counter variable which is the current
         * position the list is within. The range checking is used to see whether or not the previous
         * position exists, if it does, retrieve it and then get it's location from a geocolder helper class
         * and then get it's latitude and longitude locations from that and move the GoogleMap camera
         * to that specific location.
         *@param view
         */

        private void setBackButton(View view){
            ImageButton backButton = (ImageButton) view.findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tempCounter = currentPosition;
                    if(new Range<Integer>(0, (reminderList.size() - 1)).contains(--tempCounter)){
                        //de-increment counter position by 1
                        --currentPosition;
                    }
                    ReminderData temp = reminderList.get(currentPosition);
                    changeCursor(ReminderDatabaseHelper.get(ShowMapWithTimetable.this).getReminderData(temp.getLocation()));
                    List<Address> addresses = getLocationFromSelectedItem(currentPosition);
                    LatLng location = new LatLng(addresses.get(Constants.INITIAL_LOCATION).getLatitude(),
                            addresses.get(Constants.INITIAL_LOCATION).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(location).title(temp.getReason()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.ZOOM_LEVEL));
                }
            });

        }


    }
}

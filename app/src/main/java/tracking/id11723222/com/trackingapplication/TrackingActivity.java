package tracking.id11723222.com.trackingapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import tracking.id11723222.com.trackingapplication.CustomViews.TwoButtons;
import tracking.id11723222.com.trackingapplication.model.ReminderData;
import tracking.id11723222.com.trackingapplication.model.ReminderDatabaseHelper;
import tracking.id11723222.com.trackingapplication.services.TrackingService;

public class TrackingActivity extends AppCompatActivity {
    private Button mEmailButton;
    private TwoButtons mTriangleButtons;
    private ListView mLocationListView, mTimeListView;
    private ArrayList<LatLng> locations;
    private ArrayList<Time> times;
    private ArrayAdapter<LatLng> locationAdapter;
    private ArrayAdapter<Time> timeAdapter;

    /**
     * This method will inflate the activity_tracking layout. It will also initialise the
     * LatLng (holding the locations recorded) and the time (holding the times it was recorded) list
     * arrays. It will also set up its corresponding adapters that will expand the given list views.
     * The intent filter will also be used in conjunction with the broadcast receiver so that it will
     * listen for the specific location and time updates given by the TrackingService intent service
     * class. The intent filter's action will be set to the constant UPDATE_COMMAND; which will
     * also be set in the TrackingService's onHandleIntent method.
     *@param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        locations = new ArrayList<LatLng>();
        times = new ArrayList<Time>();
        mTimeListView = (ListView) findViewById(R.id.recordedTimes);
        mLocationListView = (ListView) findViewById(R.id.recorderLocations);
        timeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,times);
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
        mTimeListView.setAdapter(timeAdapter);
        mLocationListView.setAdapter(locationAdapter);
        setButtons();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.UPDATE_COMMAND);
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    /**
     * Unregisters the receiver so that it stops listening for updates
     *
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * Initialises and sets the custom button views and email button. The start button
     * (the red triangle) will start the Intent service named 'TrackingService' and will add the
     * interval and duration given by the user into the intent as a timer for how long the
     * service should run for. The reset button (the blue triangle) will reset the all the recorded
     * times and locations by calling it's corresponding clear function and wlll notify their
     * adapters of the changes.
     * The email button will also be used to listen for clicks
     */

    private void setButtons(){
        mTriangleButtons = (TwoButtons) findViewById(R.id.triangleButtons);
        mTriangleButtons.setOnButtonClickEvent(new TwoButtons.ButtonClickEvents() {
            @Override
            public void startButtonClick() {
                // TODO Auto-generated method stub
                //start background service
                Intent myIntent = new Intent(getBaseContext(), TrackingService.class);
                Spinner intervalSpinner = (Spinner) findViewById(R.id.intervalSpinner);
                EditText durationET = (EditText) findViewById(R.id.durationET);
                myIntent.putExtra(Constants.INTERVAL, Integer.parseInt(intervalSpinner.getSelectedItem().toString()));
                myIntent.putExtra(Constants.DURATION, Integer.parseInt(durationET.getText().toString()));
                Toast.makeText(getApplicationContext(),Constants.STARTED,Toast.LENGTH_LONG).show();
                startService(myIntent);
            }

            @Override
            public void resetButtonClick() {
                // TODO Auto-generated method stub
                //reset the data given
                locations.clear();
                times.clear();
                timeAdapter.notifyDataSetChanged();
                locationAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), Constants.CLEARED, Toast.LENGTH_SHORT).show();
            }
        });

        mEmailButton = (Button) findViewById(R.id.emailButton);
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpEmail();
            }
        });

    }

    /**
     * This will set up a new intent start an email activity with the given locations times as
     * the body of the text.
     */
    private void setUpEmail(){
        Intent newIntent = new Intent(Intent.ACTION_SEND);
        newIntent.setData(Uri.parse(Constants.MAIL_TO));
        newIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.RECORDED_LOCATIONS);
        newIntent.putExtra(Intent.EXTRA_TEXT, locations.toString() + Constants.NEW_LINE + times.toString());
        newIntent.setType(Constants.EMAIL_FORMAT);
        startActivity(Intent.createChooser(newIntent, Constants.EMAIL_ON));

    }





    /**
     * Broadcast receiver. This handles all the message coming in.
     *
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.ENTRY_CREATED, Constants.UPDATE_COMMAND);
            locations.add((LatLng) intent.getExtras().get(Constants.EXTRA_LOCATION));
            times.add((Time) intent.getExtras().get(Constants.TIME));
            timeAdapter.notifyDataSetChanged();
            locationAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), Constants.UPDATE_COMMAND, Toast.LENGTH_SHORT).show();
        }
    };








}





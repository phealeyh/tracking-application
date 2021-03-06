package tracking.id11723222.com.trackingapplication;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import tracking.id11723222.com.trackingapplication.MapActivities.GoToLocationActivity;
import tracking.id11723222.com.trackingapplication.model.ReminderData;
import tracking.id11723222.com.trackingapplication.model.ReminderDatabaseHelper;
import tracking.id11723222.com.trackingapplication.settings.LocationSettingsChecker;

public class ListTimetableActivity extends AppCompatActivity {
    private ListView mReminderListView;
    private Context mContext;

    /**
     * This will inflate the view based on the activity_list_timetable layout
     * and then initialises the list view to the reminder_list. It also sets it's adapter
     * by calling an instance of an inner helper class.
     *@param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_list_timetable);
        mContext = this;
        Intent intent = getIntent();
        String destination = intent.getStringExtra(Constants.EXTRA_LOCATION);
        mReminderListView = (ListView) findViewById(R.id.reminder_list);
        mReminderListView.setAdapter(new ReminderAdapter(this, destination));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

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
        else if(id == R.id.about_page){
            startActivity(new Intent(this,AboutPageActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ReminderAdapter extends CursorAdapter {
        private String mLocation;
        /**
         * Creates a ReminderAdapter providing a list of reminder potentially filtered by location.
         *
         * @param context
         * @param location the location to filter by, or null if all reminders should be included.
         */
        public ReminderAdapter(Context context, String location) {
            super(context, ReminderDatabaseHelper.get(ListTimetableActivity.this).getReminderData(location), 0);
            mLocation = location;
        }

        /**
         * Inflates a new view based on the given results of
         * the database cursor based on the item_reminder_data template layout
         *@param context, cursor, parent
         *@return View
         */


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_data, null);
        }

        /**
         *
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
            setDeleteButton(view,cursor);
            setLocationButton(view, cursor);

        }

        /**
         * This will set up the delete button's listener and then handle the action of it
         * being clicked. Once clicked, the reminder that view represents is deleted and
         * the next reminder in the table is selected by the cursor
         *@param view
         *@param cursor
         */


        private void setDeleteButton(View view, Cursor cursor){
            final ReminderData reminderData = ((ReminderDatabaseHelper.ReminderCursor) cursor).getReminderData();
            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.list_item_delete_imagebutton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReminderDatabaseHelper.get(ListTimetableActivity.this).removeReminder(reminderData);
                    changeCursor(ReminderDatabaseHelper.get(ListTimetableActivity.this).getReminderData(mLocation));
                    Toast.makeText(getApplicationContext(), Constants.DELETED, Toast.LENGTH_SHORT).show();

                }
            });

        }

        /**
         * This will set up the location button's listener and then handle the action of it
         * being clicked. Once clicked, the location and reason associated with that reminder
         * is passed within an intent for the GoToLocation activity.
         *
         *@param view, cursor
         */

        private void setLocationButton(View view, Cursor cursor){
            final Context context = getApplicationContext();
            final ReminderData reminderData = ((ReminderDatabaseHelper.ReminderCursor) cursor).getReminderData();
            ImageButton locationButton = (ImageButton) view.findViewById(R.id.list_item_location_imagebutton);
            locationButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(LocationSettingsChecker.checkLocationSettings(context) && LocationSettingsChecker.checkGPSSettings(context)){
                        Intent intent = new Intent(mContext, GoToLocationActivity.class);
                        intent.putExtra(Constants.EXTRA_LOCATION, reminderData.getLocation());
                        intent.putExtra(Constants.ENTRY_REASON, reminderData.getReason());
                        Toast.makeText(getApplicationContext(), Constants.LOCATION_SELECTED, Toast.LENGTH_SHORT).show();
                        //go to specific location with given coordinates
                        startActivity(intent);
                    }
                    else if(!LocationSettingsChecker.checkLocationSettings(context)){
                        Toast.makeText(getApplicationContext(), Constants.TURN_ON_LOCATION, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                    else{ //change mobile data settings
                        Toast.makeText(getApplicationContext(), Constants.TURN_ON_DATA, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(Constants.COM_SETTINGS,Constants.SUMMARY_ACTIVITY_USAGE));
                        startActivity(intent);

                    }

                }
            });

        }

    }

}
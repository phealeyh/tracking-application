package tracking.id11723222.com.trackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

import tracking.id11723222.com.trackingapplication.model.ReminderData;
import tracking.id11723222.com.trackingapplication.model.ReminderDatabaseHelper;

public class CreateTimetableActivity extends AppCompatActivity {

    /**
     * This will set the layout to the activity_create_timetable.
     *
     *@param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timetable);
    }


    /**
     * This will inflate the options menu based on the home_main menu which consists of a
     * a link to the MainActivity.
     *@param menu
     */



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Links up the home settings item to the main activity. It will start the MainActivity
     * class once it is clicked.
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
     *Once a user has submitted an entry, the entry will be put into the database
     * and all of the fields within the Edit texts will be set to blank.
     *@param view
     */


    public void onSubmitClicked(View view) {
        Toast.makeText(this, Constants.ENTRY_CREATED, Toast.LENGTH_SHORT).show();
        createEntry();
        resetFields();
    }

    /**
     * Gets all of the data from the respective edit text fields and will create a new reminderData
     * based on this information. Once the object is created, it will be pushed into the table
     * with the helper of the database helper.
     */

    private void createEntry() {
        try {
            String actual_time = getTime();
            EditText locationET = (EditText) findViewById(R.id.location_field);
            EditText dateET = (EditText) findViewById(R.id.date_field);
            Spinner reasonET = (Spinner) findViewById(R.id.reason_box);
            ReminderData reminderData = new ReminderData(Constants.ZERO, locationET.getText().toString(),
                    dateET.getText().toString(), actual_time, reasonET.getSelectedItem().toString());
            ReminderDatabaseHelper.get(this).addReminder(reminderData);
            Time currentTime = new Time(Calendar.getInstance().getTimeInMillis());
        } catch (Exception ex) {
            Log.e(Constants.ERROR, Constants.ADD_CLICKED_EXCEPTION, ex);
        }

    }

    /**
     * Private helper method that will be used to get the time plus the selected meridian value
     * from the spinner box.
     * @return String
     */

    private String getTime() {
        String[] timeTypes = getResources().getStringArray(R.array.time);
        EditText timeET = (EditText) findViewById(R.id.time_field);
        Spinner timeType = (Spinner) findViewById(R.id.time_box);
        //equal to AM string
        if (timeTypes[Constants.ZERO].equals(timeType.toString())) {
            //return time plus meridiem
            return timeET.getText().toString() + Constants.BLANK_STATE + timeTypes[Constants.ZERO];
        } else {
            //return time plus meridiem
            return timeET.getText().toString() + Constants.BLANK_STATE + timeTypes[Constants.ONE];
        }
    }

    /**
     * This will clear the text fields and show a toast that states that it has been cleared
     *
     *@param view
     */
    public void onClearClicked(View view) {
        Toast.makeText(this, Constants.CLEARED_TEXT, Toast.LENGTH_LONG).show();
        resetFields();
    }

    /**
     * Basically resets the EditText view fields to a blank state while also setting the spinner
     * to the default meridian position
     */
    private void resetFields() {
        Spinner spinner = (Spinner) findViewById(R.id.reason_box);
        EditText text = (EditText) findViewById(R.id.location_field);
        text.setText(Constants.BLANK_STATE);
        text = (EditText) findViewById(R.id.date_field);
        text.setText(Constants.BLANK_STATE);
        text = (EditText) findViewById(R.id.time_field);
        text.setText(Constants.BLANK_STATE);
        spinner.setSelection(Constants.ZERO); //sets spinner array to the first element
        spinner = (Spinner) findViewById(R.id.time_box);
        spinner.setSelection(Constants.ZERO); //sets spinner array to the first element

    }

}

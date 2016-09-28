package tracking.id11723222.com.trackingapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Time;
import java.util.Calendar;

import tracking.id11723222.com.trackingapplication.model.ReminderData;
import tracking.id11723222.com.trackingapplication.model.ReminderDatabaseHelper;

public class CreateTimetableActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{


    /**
     * This will set the layout to the activity_create_timetable.
     *
     *@param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timetable);
        setDateListener();
        setTimeListener();
        setAutoCompleteFeature();
        setTextViewListener();
    }

    private void setTextViewListener(){
        final TextView textView = (TextView) findViewById(R.id.location_field);
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){ //doesn't have focus
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });
    }

    private void setAutoCompleteFeature(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new String[]{"Australia", "Australoid" , "Belgium","Cambodia","United States of America"});
        MultiAutoCompleteTextView locationView = (MultiAutoCompleteTextView)
                findViewById(R.id.location_field);
        locationView.setAdapter(adapter);
        locationView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
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



    /**
     * This function will set the date edit text field to open up a new
     * DatePickerDialog once the user clicks on the field. When a user selects
     * a date from the dialog, it sets that text in the edit text field.
     */

    private void setDateListener(){
        final Calendar myDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                changeDate(year, monthOfYear, dayOfMonth);
                Toast.makeText(CreateTimetableActivity.this,Constants.DATE_SET,Toast.LENGTH_SHORT).show();
            }
        };
        final EditText dateField = (EditText) findViewById(R.id.date_field);
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTimetableActivity.this, dateListener, myDate
                        .get(Calendar.YEAR), myDate.get(Calendar.MONTH),
                        myDate.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    /**
     * This function will set the time edit text field to open up a new
     * TimePickerDialog once the user clicks on the field. When a user selects a given
     * time from the dialog, it sets that time to the edit text's field.
     */

    private void setTimeListener(){
        final Time currentTime = new Time(System.currentTimeMillis());
        final TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                changeTime(hourOfDay, minute);
                Toast.makeText(CreateTimetableActivity.this,Constants.TIME_SET,Toast.LENGTH_SHORT).show();
            }
        };
        final EditText timeField = (EditText) findViewById(R.id.time_field);
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateTimetableActivity.this, timeListener, currentTime.getHours(),
                        currentTime.getMinutes(), false).show();
            }
        });


    }


    /**
     * Helper method that sets the edit text date field to the paramaters given.
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */

    private void changeDate(int year, int monthOfYear, int dayOfMonth){
        String formattedDate = dayOfMonth + "/" + monthOfYear + "/"  + year;
        EditText dateField = (EditText) findViewById(R.id.date_field);
        dateField.setText(formattedDate, TextView.BufferType.EDITABLE);
    }
    /**
     * Helper method that sets the edit text time field to the paramaters given.
     * @param hourOfDay
     * @param minute
     */

    private void changeTime(int hourOfDay, int minute){
        String formattedDate = hourOfDay + ":" + minute;
        EditText timeField = (EditText) findViewById(R.id.time_field);
        timeField.setText(formattedDate, TextView.BufferType.EDITABLE);
    }



    /**
     * Gets all of the data from the respective edit text fields and will create a new reminderData
     * based on this information. Once the object is created, it will be pushed into the table
     * with the helper of the database helper.
     */

    private void createEntry() {
        try {
            EditText timeET = (EditText) findViewById(R.id.time_field);
            EditText locationET = (EditText) findViewById(R.id.location_field);
            EditText dateET = (EditText) findViewById(R.id.date_field);
            Spinner reasonET = (Spinner) findViewById(R.id.reason_box);
            ReminderData reminderData = new ReminderData(Constants.ZERO, locationET.getText().toString(),
                    dateET.getText().toString(), timeET.getText().toString(), reasonET.getSelectedItem().toString());
            ReminderDatabaseHelper.get(this).addReminder(reminderData);
        } catch (Exception ex) {
            Log.e(Constants.ERROR, Constants.ADD_CLICKED_EXCEPTION, ex);
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
     *Once a user has submitted an entry, the entry will be put into the database
     * and all of the fields within the Edit texts will be set to blank.
     *@param view
     */


    public void onSubmitClicked(View view) {
        //only submit if the fields are all filled with something
        if(allFieldsHaveText()) {
            Toast.makeText(this, Constants.ENTRY_CREATED, Toast.LENGTH_SHORT).show();
            createEntry();
            resetFields();
        }
        else{
            Toast.makeText(this,Constants.FILL_TEXT, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method will return true if all the fields in the text fields are filled
     * or false if all/one of them is missing their respective entry text
     */

    private boolean allFieldsHaveText(){
        EditText timeET = (EditText) findViewById(R.id.time_field);
        EditText locationET = (EditText) findViewById(R.id.location_field);
        EditText dateET = (EditText) findViewById(R.id.date_field);
        return ((!timeET.getText().toString().isEmpty()) && (!locationET.getText().toString().isEmpty()) && (!dateET.getText().toString().isEmpty()));
    }


    /**
     * Basically resets the EditText view fields to a blank state while also setting the spinner
     * to the default meridian position
     */
    private void resetFields() {
        Spinner spinner = (Spinner) findViewById(R.id.reason_box);
        EditText text = (EditText) findViewById(R.id.location_field);
        text.setText("");
        text.setHint(text.getHint());

        text = (EditText) findViewById(R.id.date_field);
        text.setText("");
        text.setHint(text.getHint());

        text = (EditText) findViewById(R.id.time_field);
        text.setText("");
        text.setHint(text.getHint());
        spinner.setSelection(Constants.ZERO); //sets spinner array to the first element

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("TAG: ", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();

    }




}

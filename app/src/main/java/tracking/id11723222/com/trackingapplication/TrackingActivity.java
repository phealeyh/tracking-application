package tracking.id11723222.com.trackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

public class TrackingActivity extends AppCompatActivity {

    private Button mStartButton, mResetButton, mEmailButton;
    private Chronometer mIntervalChronomter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        setButtonListeners();
        setChronometer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
     * This method will set each button listener to handle it's respective action events.
     * If the start button is clicked, the TrackingService Intent is started along which will
     * track the user's coordinate at each interval.
     * The Reset values button will reset all of the values inside the lists
     * The Email button will open up a new email intent and allow the user to email the locations
     * and times recorded.
     */

    private void setButtonListeners(){
        mStartButton = (Button)findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start service
                Toast.makeText(getApplicationContext(),Constants.STARTED,Toast.LENGTH_LONG).show();
            }
        });
        mResetButton = (Button)findViewById(R.id.resetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),Constants.CLEARED,Toast.LENGTH_LONG).show();
            }
        });
        mEmailButton = (Button) findViewById(R.id.emailButton);
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),Constants.EMAIL_SENT,Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Sets the chronometer to time the intervals given.
     */
    private void setChronometer(){
        mIntervalChronomter = (Chronometer) findViewById(R.id.interval_timer);
        //set it to the interval given
        mIntervalChronomter.setBase(5000L);
    }


}

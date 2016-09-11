package tracking.id11723222.com.trackingapplication.model;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by tranc on 9/09/2016.
 */

public class LocationData implements Serializable {

    public static final String TABLE = "location";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_TIME = "time";
    public static final String[] COLUMNS = {COLUMN_ID, COLUMN_LOCATION, COLUMN_TIME};

    //private member variables for the columns
    private int mId;
    private String mLocation;
    private String mTime;

    public LocationData(int id, String location, String time) {
        mId = id;
        mLocation = location;
        mTime = time;
    }


    public LocationData(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        mLocation = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
        mTime = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    @Override
    public String toString(){
        return "Time: " + mTime + " at " + mLocation;
    }


}







package tracking.id11723222.com.trackingapplication.model;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by phealeyhang on 20/10/15.
 */

public class ReminderData implements Serializable {
    //constants
    public static final String TABLE = "reminder";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_REASON = "reason";
    public static final String[] COLUMNS = {COLUMN_ID, COLUMN_LOCATION, COLUMN_DATE, COLUMN_TIME, COLUMN_REASON};
    //private member variables for the columns
    private int mId;
    private String mLocation;
    private String mDate;
    private String mTime;
    private String mReason;

    /**
     * Constructor to create a ReminderData object with the data.
     *
     * @param id       the unique id of this instance. Use zero to generate a new id.
     * @param location
     * @param date
     * @param time
     * @param reason
     */

    public ReminderData(int id, String location, String date, String time, String reason) {
        mId = id;
        mLocation = location;
        mDate = date;
        mTime = time;
        mReason = reason;
    }


    /**
     * Constructor to create a ReminderData object with data from a cursor.
     *
     * @param cursor
     */

    public ReminderData(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        mLocation = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
        mDate = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        mTime = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
        mReason = cursor.getString(cursor.getColumnIndex(COLUMN_REASON));
    }

    /**
     * This will return the mId of the reminderData
     *
     *@return mId
     */


    public int getId() {
        return mId;
    }

    /**
     * This will set the member id
     * to the given id within the argument
     *@param id
     *
     */


    public void setId(int id) {
        mId = id;
    }

    /**
     * Gets the specified location
     *@return String
     */


    public String getLocation() {
        return mLocation;
    }

    /**
     * This method sets the reminder location
     *
     *@param location
     */


    public void setLocation(String location) {
        mLocation = location;
    }

    /**
     * This will get the date scheduled for
     * the reminder
     *@return String
     */


    public String getDate() {
        return mDate;
    }

    /**
     * This will set the member date to whatever
     * date is given for the reminder
     *@param date
     */

    public void setDate(String date) {
        mDate = date;
    }

    /**
     *This will return the set time of the reminder
     *
     *@return mTime
     */


    public String getTime() {
        return mTime;
    }

    /**
     *This method will get the time associated with
     * the ReminderTV
     *@param time
     */


    public void setTime(String time) {
        mTime = time;
    }

    /**
     * This will retrieve the reason for a given
     * reminder entry
     *
     *@return String
     */


    public String getReason() {
        return mReason;
    }


    /**
     *This will set the reminder's reason
     * to whatever is given as an argument.
     *@param reason
     *
     */

    public void setReason(String reason) {
        mReason = reason;
        mReason = reason;
    }

}

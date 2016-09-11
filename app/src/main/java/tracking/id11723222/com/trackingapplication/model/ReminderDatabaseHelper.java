package tracking.id11723222.com.trackingapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phealeyhang on 20/10/15.
 */
public class ReminderDatabaseHelper extends SQLiteOpenHelper {

    //The database version. This must be incremented every time the database structure changes.
    private static final int DATABASE_VERSION = 1;

    // The name of the file in which the database is stored.
    private static final String DATABASE_NAME = "reminder.db";

    // Singleton instance of this helper
    private static ReminderDatabaseHelper instance;

    /**
     * Access a singleton instance of this class.
     *
     * @param context
     * @return ReminderDatabaseHelper
     */

    public static synchronized ReminderDatabaseHelper get(Context context) {
        if (instance == null) {
            // Make sure we're using the application context for a longer scope
            instance = new ReminderDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Creates the ReminderDatabaseHelper. This is private, so do not use this constructor directly.
     * Instead, use the get() method to access the singleton.
     * @param context
     */
    private ReminderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, new ReminderCursorFactory(), DATABASE_VERSION);

    }

    /**
     * This is called if the the database file is nonexistent (e.g. the app was just installed).
     * Here, we create the reminder table.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create reminder table
        String createReminderSql = "create table " + ReminderData.TABLE + " ("
                + ReminderData.COLUMN_ID + " integer primary key autoincrement, "
                + ReminderData.COLUMN_LOCATION + " text not null, "
                + ReminderData.COLUMN_DATE + " text not null, "
                + ReminderData.COLUMN_TIME + " text not null, "
                + ReminderData.COLUMN_REASON + " text not null)";
        //create location table
        String createLocationSql = "create table " + LocationData.TABLE + " ("
                + LocationData.COLUMN_ID + " integer primary key autoincrement, "
                + LocationData.COLUMN_LOCATION + " text not null, "
                + LocationData.COLUMN_TIME + " text not null)";
        db.execSQL(createReminderSql);
        db.execSQL(createLocationSql);

    }

    /**
     * This is called if you increment DATABASE_VERSION.
     * Here, you should alter the tables currently stored on the device to match the new
     * schema required by this new version of the app.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Get a list of all reminder data from the database.
     * @param name optional filter to show only results with this name. Use null to list all.
     * @return the list
     */
    public List<ReminderData> getReminderDataList(String name) {
        List<ReminderData> list = new ArrayList<>();
        ReminderCursor cursor = getReminderData(name);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getReminderData());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * Get a cursor over reminder data from the database.
     *
     * @param location optional filter to show only results with this name. Use null to list all.
     * @return a ReminderData
     */
    public ReminderCursor getReminderData(String location) {
        // Default to showing all reminder data
        String where = null;
        String[] whereArgs = null;
        // If a name is specified, filter the results
        if (location != null) {
            where = ReminderData.COLUMN_LOCATION + " = ?";
            whereArgs = new String[]{location};
        }
        return (ReminderCursor) getReadableDatabase().query(ReminderData.TABLE, ReminderData.COLUMNS, where, whereArgs, null, null, null);
    }

    public ReminderCursor getLocationData(String location) {
        // Default to showing all location data
        String where = null;
        String[] whereArgs = null;
        // If a name is specified, filter the results
        if (location != null) {
            where = LocationData.COLUMN_LOCATION + " = ?";
            whereArgs = new String[]{location};
        }
        return (ReminderCursor) getReadableDatabase().query(LocationData.TABLE, LocationData.COLUMNS, where, whereArgs, null, null, null);
    }


    /**
     * Add a ReminderData row to the database.
     *
     * @param reminderData the data to add to the database.
     */

    public void addReminder(ReminderData reminderData) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReminderData.COLUMN_LOCATION, reminderData.getLocation());
        values.put(ReminderData.COLUMN_DATE, reminderData.getDate());
        values.put(ReminderData.COLUMN_TIME, reminderData.getTime());
        values.put(ReminderData.COLUMN_REASON, reminderData.getReason());
        db.insertOrThrow(ReminderData.TABLE, null, values);
    }

    /**
     * Add a locationData row to the database.
     *
     * @param locationData the data to add to the database.
     */

    public void addLocation(LocationData locationData) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationData.COLUMN_LOCATION, locationData.getmLocation());
        values.put(LocationData.COLUMN_TIME, locationData.getmTime());
        db.insertOrThrow(LocationData.TABLE, null, values);
    }


    /**
     * Remove a ReminderData row from the database.
     *
     * @param reminderData
     */
    public void removeReminder(ReminderData reminderData) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ReminderData.TABLE, ReminderData.COLUMN_ID + " = ?", new String[]{String.valueOf(reminderData.getId())});
    }

    /**
     * A custom cursor factory which creates instances of ReminderCursor.
     */
    private static class ReminderCursorFactory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
            return new ReminderCursor(masterQuery, editTable, query);
        }
    }


    /**
     * A custom cursor providing a getReminderData() method to return the current row as a Java object.
     * This encapsulates the database structure behind a plain old Java object (POJO).
     */
    public static class ReminderCursor extends SQLiteCursor {
        public ReminderCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
            super(driver, editTable, query);
        }

        public ReminderData getReminderData() {
            return new ReminderData(this);
        }

        public LocationData getLocationData() {
            return new LocationData(this);
        }




    }








}

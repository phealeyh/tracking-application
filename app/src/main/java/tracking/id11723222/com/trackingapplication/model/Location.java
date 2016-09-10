package tracking.id11723222.com.trackingapplication.model;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;

/**
 * Created by tranc on 9/09/2016.
 */

public class Location {

    /*Private instance variables */
    Time time;
    LatLng location;

    public Location(LatLng location, Time time){
        this.location = location;
        this.time = time;
    }

    public String toString(){
        return "Time: " + time
                + " at " + location;
    }


    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }



}

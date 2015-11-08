package tracking.id11723222.com.trackingapplication;

/**
 * Created by phealeyhang on 8/11/15.
 */
public class TimeUnit {

    private enum TIME {
        Seconds,
        Minutes,
        Hours}

    private static TIME timeUnit = TIME.Seconds;


    public static void changeTime(String preference){
        if(preference.equals(TIME.Seconds.toString())){
            timeUnit = TIME.Seconds;
        }
        else if(preference.equals(TIME.Minutes.toString())){
            timeUnit = TIME.Minutes;
        }
        else if(preference.equals(TIME.Hours.toString())){
            timeUnit = TIME.Hours;
        }
    }

    public static TIME getTimeUnit(){
        return timeUnit;
    }


}

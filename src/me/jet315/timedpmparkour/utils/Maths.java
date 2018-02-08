package me.jet315.timedpmparkour.utils;

/**
 * Created by Jet on 22/12/2017.
 */
public class Maths {

    /**
     * @param minutes as days:hours:minutes - 00:00:00
     * @return
     */
    public static String[] splitToComponentTimes(int minutes) {
        String[] timePlayed = new String[3];

        int daysUnFormatted = minutes/1440;

        int hoursUnFormatted = minutes/60 - (daysUnFormatted *24);

        int minsUnFormatted = minutes - (( hoursUnFormatted*60) + (daysUnFormatted*1440));


        if(daysUnFormatted <= 9){
            String days = "0" + daysUnFormatted;
            timePlayed[0] = days;
        }else{
            String days = Integer.toString(daysUnFormatted);
            timePlayed[0] = days;

        }
        if(hoursUnFormatted <= 9){
            String hours = "0" + hoursUnFormatted;
            timePlayed[1] = hours;
        }else{
            String hours = Integer.toString(hoursUnFormatted);
            timePlayed[1] = hours;
        }

        if(minsUnFormatted <= 9){
            String mins = "0" + minsUnFormatted;
            timePlayed[2] = mins;
        }else{
            String mins = Integer.toString(minsUnFormatted);
            timePlayed[2] = mins;
        }

        return timePlayed;
    }

    /**
     * @param milliseconds as days:hours:minutes:seconds - 00:00:00:00
     * @return
     */
    public static String[] splitToComponentTimes(long milliseconds) {
        String[] timePlayed = new String[4];

        int daysUnFormatted = (int) (milliseconds / (1000 * 60 * 60 * 24));

        int hoursUnFormatted = (int) ((milliseconds / (1000*60*60)) % 24);

        int minsUnFormatted = (int) ((milliseconds / (1000*60)) % 60);

        int secondsUnFormated = (int) (milliseconds / 1000) % 60 ;

        if(daysUnFormatted <= 9){
            String days = "0" + daysUnFormatted;
            timePlayed[0] = days;
        }else{
            String days = Integer.toString(daysUnFormatted);
            timePlayed[0] = days;

        }

        if(hoursUnFormatted <= 9){
            String hours = "0" + hoursUnFormatted;
            timePlayed[1] = hours;
        }else{
            String hours = Integer.toString(hoursUnFormatted);
            timePlayed[1] = hours;

        }
        if(minsUnFormatted <= 9){
            String minutes = "0" + minsUnFormatted;
            timePlayed[2] = minutes;
        }else{
            String minutes = Integer.toString(minsUnFormatted);
            timePlayed[2] = minutes;
        }

        if(secondsUnFormated <= 9){
            String secs = "0" + secondsUnFormated;
            timePlayed[3] = secs;
        }else{
            String secs = Integer.toString(secondsUnFormated);
            timePlayed[3] = secs;
        }

        return timePlayed;
    }


}

package app.com.listvv;

/**
 * Created by Rohit on 6/9/2017.
 */

public class FormatTIme {

    private int hour, minute;
    private boolean PM_FLAG=false;
    private String time;

    FormatTIme(int hour, int minute){
        this.hour = hour;
        this.minute = minute;

        setTime();
    }

    private void setTime(){
        String h = getHour();

        if(PM_FLAG){
            time = h +" : "+getMinute()+" PM";
        }else{
            time = h +" : "+getMinute()+" AM";
        }
    }

    private String getHour(){
        if(hour == 0){
            hour = 12;
        }
        else if(hour == 12){
            PM_FLAG = true;
        }
        else if(hour > 12){
            hour = hour - 12;
            PM_FLAG = true;
        }

        if(hour >0 && hour < 10){
            return "0"+hour;
        }else{
            return ""+hour;
        }
    }

    private String getMinute(){
        if(minute >= 0 && minute < 10){
            return "0"+minute;
        }else{
            return minute+"";
        }
    }

    public String getTime(){
        return time;
    }

}

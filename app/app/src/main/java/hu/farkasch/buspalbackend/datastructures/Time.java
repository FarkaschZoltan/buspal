package hu.farkasch.buspalbackend.datastructures;

public class Time{
    private int hour;
    private int minute;
    private int second;

    public Time(){ //def, constructor is always 0, 0, 0
        hour = 0;
        minute = 0;
        second = 0;
    }

    public Time(int hour, int minute, int second){ //assumeing only correct values
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public Time(String timeData){ //cunstructing with the help of a string, delimited with ":", assumeing only correct values
        String[] timeArray = timeData.split(":");
        hour = Integer.parseInt(timeArray[0]);
        minute = Integer.parseInt(timeArray[1]);
        second = Integer.parseInt(timeArray[2]);
    }

    //basic getters and setters

    public int getHour(){
        return hour;
    }

    public int getMinute(){
        return minute;
    }

    public int getSecond(){
        return second;
    }

    public void setHour(int hour){
        this.hour = hour;
    }

    public void setMinute(int minute){
        this.minute = minute;
    }

    public void setSecond(int second){
        this.second = second;
    }

    public void add(int secondsToAdd){ // converting the seconds int hour:minute:second, then calliing add(Time);
        int hourData = secondsToAdd / 3600;
        int minuteData = (secondsToAdd - hourData * 3600) / 60;
        int secondData = secondsToAdd - hourData * 3600 - minuteData * 60;
        Time timeToAdd = new Time(hourData, minuteData, secondData);
        this.add(timeToAdd);
    }

    public void add(Time timeToAdd){ //adding two Time-s together
        int newSecond = (second + timeToAdd.getSecond()) % 60;
        int newMinute = (minute + timeToAdd.getMinute() + (second + timeToAdd.getSecond()) / 60) % 60;
        int newHour = (hour + timeToAdd.getHour() + (minute + timeToAdd.getMinute() + (second + timeToAdd.getSecond()) / 60) / 60) % 24;
        second = newSecond;
        minute = newMinute;
        hour = newHour;
    }

    @Override
    public String toString(){
        return String.format(hour + ":" + minute + ":" + second);
    }

}
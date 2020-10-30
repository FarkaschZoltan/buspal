package hu.farkasch.buspalbackend.datastructures;

public class Time implements Comparable<Time>{
    private int hour;
    private int minute;
    private int second;

    public Time(){ //def, constructor is always 0, 0, 0
        hour = 0;
        minute = 0;
        second = 0;
    }

    public Time(int hour, int minute, int second){ //assuming only correct values
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public Time(String timeData){ //constructing with the help of a string, delimited with ":", assuming only correct values
        String[] timeArray = timeData.split(":");
        hour = Integer.parseInt(timeArray[0]);
        minute = Integer.parseInt(timeArray[1]);
        if(timeArray.length > 2){ //for when timeData is only hh:mm
            second = Integer.parseInt(timeArray[2]);
        }

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
        String time = "";

        if(hour < 10){
            time += "0" + hour;
        }
        else if(hour > 24){
            time += "0" + (hour-24);
        }
        else{
            time += hour;
        }

        time += ":";
        if(minute < 10){
            time += "0" + minute;
        }
        else{
            time += minute;
        }

        return time;
        //return String.format(hour + ":" + minute + ":" + second);
    }

    @Override
    public int compareTo(Time t) {
        if(hour > t.hour){
            return 1;
        }
        else if (hour < t.hour){
            return -1;
        }
        else {
            if(minute > t.minute){
                return 1;
            }
            if (minute < t.minute){
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }

        if(!(o instanceof Time)){
            return false;
        }

        Time t = (Time) o;

        if(hour == t.hour && minute == t.minute){
            return true;
        }
        else {
            return false;
        }
    }
}
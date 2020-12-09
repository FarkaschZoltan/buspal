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

    public Time(String timeData){
        //constructing with the help of a string, delimited with ":", assuming only correct values
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
        int newHour = (hour + timeToAdd.getHour() + (minute + timeToAdd.getMinute() +
                (second + timeToAdd.getSecond()) / 60) / 60) % 24;
        second = newSecond;
        minute = newMinute;
        hour = newHour;
    }

    public Time subtract(Time timeToSubtract){
        int seconds = this.second - timeToSubtract.second;
        int minutes = 0;
        int hours = 0;

        if(seconds < 0){
            seconds += 60;
            minutes -= 1;
        }

        minutes += this.minute - timeToSubtract.minute;

        if(minutes < 0){
            minutes += 60;
            hours -= 1;
        }

        hours += this.hour - timeToSubtract.hour;

        return new Time(hours, minutes, seconds);
    }

    public boolean isInsideInterval(Time that, int hours){
        Time difference = that.subtract(this);
        boolean l = true;
        l = l && difference.hour >= 0 && difference.hour < hours;
        if(difference.hour == 0){
            l = l && difference.minute > 1;
        }

        return l;
    }

    @Override
    public String toString(){
        String time = "";

        if(hour < 10){
            time += "0" + hour;
        } else{
            time += hour;
        }

        time += ":";
        if(minute < 10){
            time += "0" + minute;
        } else{
            time += minute;
        }

        return time;
        //return String.format(hour + ":" + minute + ":" + second);
    }

    public String toString(boolean a){
            String time = "";

            if(hour < 10){
                time += "0" + hour;
            } else if(hour >= 24){
                time += "0" + (hour-24);
            } else{
                time += hour;
            }

            time += ":";
            if(minute < 10){
                time += "0" + minute;
            } else{
                time += minute;
            }
            return time;
    }

    @Override
    public int compareTo(Time t) {
        if(hour > t.hour){
            return 1;
        } else if (hour < t.hour){
            return -1;
        } else {
            if(minute > t.minute){
                return 1;
            }

            if (minute < t.minute){
                return -1;
            } else {
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

        return hour == t.hour && minute == t.minute;
    }
}
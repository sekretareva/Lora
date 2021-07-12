package com.example.loraapp;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Timestamp {
    String day, month, year, hour, minute, sec;

    public Timestamp(String timestamp) throws ParseException {
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        SimpleDateFormat minute = new SimpleDateFormat("mm");
        SimpleDateFormat sec = new SimpleDateFormat("ss");

        this.day = day.format( new SimpleDateFormat("y-M-d H:m:s.S").parse(timestamp));
        this.month = month.format( new SimpleDateFormat("y-M-d H:m:s.S").parse(timestamp));
        this.year = year.format( new SimpleDateFormat("y-M-d H:m:s.S").parse(timestamp));
        this.hour = hour.format( new SimpleDateFormat("y-M-d H:m:s.S").parse(timestamp));
        this.minute = minute.format( new SimpleDateFormat("y-M-d H:m:s.S").parse(timestamp));
        this.sec = sec.format( new SimpleDateFormat("y-M-d H:m:s.S").parse(timestamp));
    }

    public int secDif(Timestamp t){
        int sec1 = Integer.parseInt(t.sec) + Integer.parseInt(t.minute)*60 + Integer.parseInt(t.hour)*24;
        int sec2 = Integer.parseInt(this.sec) + Integer.parseInt(this.minute)*60 + Integer.parseInt(this.hour)*24;
        return Math.abs(sec2-sec1);
    }

    public boolean isPeriodDif(Timestamp t, int period){
        return this.secDif(t) == period;
    }

    public void nextTimestamp(int period){
        this.sec = String.valueOf((Integer.parseInt(this.sec) + period%60));
        this.minute = String.valueOf((Integer.parseInt(this.minute) + (int)(period/60)%60));
        this.hour = String.valueOf(Integer.parseInt(this.hour) + (int)(period/3600));
    }
}

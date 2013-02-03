package org.isfit.festival.programme.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class will hopefully grow into something similar to Time in Ruby. For
 * now it contains some convenience methods for Date-parsing from an
 * ISO8601-String into a human-readable date and time representation.
 * 
 * For now this only supports parsing UTC. It is largely untested.
 * 
 */
public class RubyTime implements Comparable<RubyTime> {
    
    private Calendar calendar;

    /**
     * Get a {@link RubyTime} instance at the current time from an ISO8601 formatted String.
     * @param iso8601 A String formatted as an ISO8601 String
     * @throws ParseException If the parse fails somehow.
     */
    public RubyTime(String iso8601) throws ParseException {
        this.calendar = toCalendar(iso8601);
    }
    
    public RubyTime(Calendar calendar) {
        this.calendar = calendar;
    }
    
    public void setTimeZone(TimeZone timeZone) {
        calendar.setTimeZone(timeZone);
    }
    
    
    /** Transform Calendar to ISO 8601 string. */
    private static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    /**
     * Get the current time as a RubyTime object.
     * @return A RubyTime object containing the time at creation.
     */
    public static RubyTime now() {
        RubyTime time = new RubyTime(Calendar.getInstance());
        return time;
    }

    /** Transform ISO 8601 string to Calendar. */
    private Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = Calendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }
    
    @Override
    public String toString() {
        return fromCalendar(calendar);
    }
    
    /**
     * Returns the time for this object as an String formatted as HH:MM.
     */
    public String toTimeAsString() {
        StringBuilder formattedTimeBuilder = new StringBuilder();
        formattedTimeBuilder.append(calendar.get(Calendar.HOUR_OF_DAY));
        
        formattedTimeBuilder.append(':');
        
        int minute = calendar.get(Calendar.MINUTE);
        String zeroPaddedMinute = "" + minute;
        if (minute < 10) {
            zeroPaddedMinute = "0" + minute;
        }
        formattedTimeBuilder.append(zeroPaddedMinute);
        
        return formattedTimeBuilder.toString();
    }

    @Override
    public int compareTo(RubyTime another) {
        return this.calendar.compareTo(another.calendar);
    }
}

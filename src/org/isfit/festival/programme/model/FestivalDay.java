package org.isfit.festival.programme.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FestivalDay implements Comparable<FestivalDay>{
    private String festivalDayString;

    public FestivalDay(String festivalDayString) {
        this.festivalDayString = festivalDayString;
    }
    
    public String getFestivalDayString() {
        return festivalDayString;
    }

    @Override
    public int compareTo(FestivalDay another) {
        if (this.getFestivalDayString().equals("All festival") && another.getFestivalDayString().equals("All festival")) {
            return 0;
        } else if (this.getFestivalDayString().equals("All festival")){
            return -1;
        } else if (another.getFestivalDayString().equals("All festival")) {
            return 1;
        }
        else {
            Pattern pattern = Pattern.compile("\\d{1,2}");
            Matcher thisMatcher = pattern.matcher(this.getFestivalDayString());
            Matcher anotherMatcher = pattern.matcher(another.getFestivalDayString());
            thisMatcher.find();
            anotherMatcher.find();
            
            int thisNumber = Integer.parseInt(thisMatcher.group(0));
            int anotherNumber = Integer.parseInt(anotherMatcher.group(0));
            
            return thisNumber - anotherNumber;
        }
    }
    
    @Override
    public int hashCode() {
        return festivalDayString.hashCode();
    }
    
}

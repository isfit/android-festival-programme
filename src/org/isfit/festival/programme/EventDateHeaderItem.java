package org.isfit.festival.programme;

import org.isfit.festival.programme.model.FestivalDay;

public class EventDateHeaderItem implements EventListItem {
    
    private String festivalDay;
    
    public EventDateHeaderItem(FestivalDay festivalDay) {
        this.festivalDay = festivalDay.getFestivalDayString();
    }
    
    public String getFestivalDay() {
        return festivalDay;
    }

    @Override
    public boolean isDateHeader() {
        return true;
    }

}

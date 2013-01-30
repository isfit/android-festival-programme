package org.isfit.festival.programme.model;

import java.util.ArrayList;

import org.json.JSONObject;

public class Event {
    
    // required
    private final EventPlace eventPlace;
    private final EventType eventType;
    private final String title;
    
    // optional
    private final String frontImageURL, bodyAsHTML, festivalDay;
    private final ArrayList<EventDate> eventDates;
    private final int priceMember, priceOther;
    private final boolean allFestival;
    
    /**
     * Creates an immutable instance of Event. This is handy since we are working
     * in a concurrent system, where updates might happen right and left. We can safely 
     * throw this class around without worrying about concurrent operations and state.
     * 
     * (dagingaa): this might make it a bit harder to do the ListView though, we'll see.
     * @param builder
     */
    private Event(Builder builder) {
        this.eventPlace = builder.eventPlace;
        this.eventType = builder.eventType;
        this.title = builder.title;
        this.frontImageURL = builder.frontImageURL;
        this.bodyAsHTML = builder.bodyAsHTML;
        this.festivalDay = builder.festivalDay;
        this.eventDates = builder.eventDates;
        this.priceMember = builder.priceMember;
        this.priceOther = builder.priceOther;
        this.allFestival = builder.allFestival;
    }
    
    private static class Builder {
        private ArrayList<EventDate> eventDates;
        private EventPlace eventPlace;
        private EventType eventType;
        private String title, frontImageURL, bodyAsHTML, festivalDay;
        private int priceMember, priceOther;
        private boolean allFestival;
        
        public Builder(String title, EventPlace eventPlace, EventType eventType) {
            this.title = title;
            this.eventPlace = eventPlace;
            this.eventType = eventType;
        }

        public Builder setEventDates(ArrayList<EventDate> eventDates) {
            this.eventDates = eventDates;
            return this;
        }

        public Builder setEventPlace(EventPlace eventPlace) {
            this.eventPlace = eventPlace;
            return this;
        }

        public Builder setEventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setFrontImageURL(String frontImageURL) {
            this.frontImageURL = frontImageURL;
            return this;
        }

        public Builder setBodyAsHTML(String bodyAsHTML) {
            this.bodyAsHTML = bodyAsHTML;
            return this;
        }

        public Builder setFestivalDay(String festivalDay) {
            this.festivalDay = festivalDay;
            return this;
        }

        public Builder setPriceMember(int priceMember) {
            this.priceMember = priceMember;
            return this;
        }

        public Builder setPriceOther(int priceOther) {
            this.priceOther = priceOther;
            return this;
        }

        public Builder setAllFestival(boolean allFestival) {
            this.allFestival = allFestival;
            return this;
        }
        
    }
    
    /**
     * Parses a JSONObject into an Event, with all required subclasses where
     * applicable.
     * 
     * @param eventJSON
     *            The {@link JSONObject} to be parsed.
     * @return A brand-spanking new Event with all subclasses.
     * @throws IllegalArgumentException 
     */
    public static Event fromJSON(JSONObject eventJSON) throws IllegalArgumentException {
        
        return null;
        
    }
    
}

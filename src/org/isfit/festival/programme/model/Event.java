package org.isfit.festival.programme.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Event {

    // required
    private final EventPlace eventPlace;
    private final EventType eventType;
    private final String title;

    // optional
    private final String frontImageURL, bodyAsHTML, festivalDay;
    private final List<EventDate> eventDates;
    private final String priceMember, priceOther;
    private final boolean allFestival;

    /**
     * Creates an immutable instance of Event. This is handy since we are
     * working in a concurrent system, where updates might happen right and
     * left. We can safely throw this class around without worrying about
     * concurrent operations and state.
     * 
     * (dagingaa): this might make it a bit harder to do the ListView though,
     * we'll see.
     * 
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
        private List<EventDate> eventDates;
        private EventPlace eventPlace;
        private EventType eventType;
        private String title, frontImageURL, bodyAsHTML, festivalDay;
        private String priceMember, priceOther;
        private boolean allFestival;

        public Builder(String title, EventPlace eventPlace, EventType eventType) {
            this.title = title;
            this.eventPlace = eventPlace;
            this.eventType = eventType;
        }

        public Builder setEventDates(List<EventDate> eventDates) {
            this.eventDates = eventDates;
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

        public Builder setPriceMember(String priceMember) {
            this.priceMember = priceMember;
            return this;
        }

        public Builder setPriceOther(String priceOther) {
            this.priceOther = priceOther;
            return this;
        }

        public Builder setAllFestival(boolean allFestival) {
            this.allFestival = allFestival;
            return this;
        }

        public Event build() {
            return new Event(this);
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
    public static Event fromJSON(JSONObject eventJSON)
            throws IllegalArgumentException {
        Support.checkNotNull(eventJSON);
        Event event = null;
        try {
            EventType eventType = EventType.fromJSON(eventJSON
                    .getJSONObject("event_type"));
            EventPlace eventPlace = EventPlace.fromJSON(eventJSON
                    .getJSONObject("event_place"));
            JSONArray eventDatesArray = eventJSON.getJSONArray("event_dates");
            List<EventDate> eventDates = new ArrayList<EventDate>();
            for (int i = 0; i < eventDatesArray.length(); i++) {
                JSONObject eventDateJSON = eventDatesArray.getJSONObject(i);
                EventDate eventDate = EventDate.fromJSON(eventDateJSON);
                eventDates.add(eventDate);
            }
            String title = eventJSON.getString("title");
            Builder eventBuilder = new Builder(title, eventPlace, eventType);
            event = eventBuilder.setBodyAsHTML(eventJSON.getString("body_as_html"))
                    .setEventDates(eventDates)
                    .setFestivalDay(eventJSON.getString("festival_day"))
                    .setFrontImageURL(eventJSON.getString("front_image_url"))
                    .setPriceMember(eventJSON.getString("price_member"))
                    .setPriceOther(eventJSON.getString("price_other"))
                    .build();

        } catch (JSONException e) {
            Log.d(Support.DEBUG + "_json", "JSON parsing failed");
            e.printStackTrace();
        }
        return event;
    }

}

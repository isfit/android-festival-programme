package org.isfit.festival.programme.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.isfit.festival.programme.EventListItem;
import org.isfit.festival.programme.util.Support;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class Event implements EventListItem {

    // required
    private final EventPlace eventPlace;
    private final EventType eventType;
    private final String title;
    private final int id;

    // optional
    private final String frontImageURL, bodyAsHTML, festivalDay;
    private final List<EventDate> eventDates;
    private final String priceMember, priceOther;
    private final boolean allFestival;
    private Bitmap frontImage;

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
        this.id = builder.id;
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
        private int id;

        public Builder(int id,String title, EventPlace eventPlace, EventType eventType) {
            this.id = id;
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
    
    public EventPlace getEventPlace() {
        return eventPlace;
    }



    public EventType getEventType() {
        return eventType;
    }



    public String getTitle() {
        return title;
    }



    public String getFrontImageURL() {
        return frontImageURL;
    }



    public String getBodyAsHTML() {
        return bodyAsHTML;
    }


    /**
     * Human-readable festival day formatting as string
     */
    public String getFestivalDay() {
        return festivalDay;
    }



    public List<EventDate> getEventDates() {
        return eventDates;
    }



    public String getPriceMember() {
        return priceMember;
    }



    public String getPriceOther() {
        return priceOther;
    }



    public boolean isAllFestival() {
        return allFestival;
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
            int id = eventJSON.getInt("id");
            Builder eventBuilder = new Builder(id, title, eventPlace, eventType);
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



    public void loadImageBitmap(ImageView frontImageView) {
        ImageLoader.getInstance().displayImage("http://events.isfit.org" + frontImageURL, frontImageView);
    }



    @Override
    public boolean isDateHeader() {
        return false;
    }



    public String getEventTime() {
        return eventDates.get(0).getStartAt();
    }
    
    public int getId() {
        return this.id;
    }

}

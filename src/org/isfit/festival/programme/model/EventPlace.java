package org.isfit.festival.programme.model;

import org.isfit.festival.programme.util.Support;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class EventPlace {

    private final String name, longitude, latitude;

    /**
     * Creates a new EventPlace instance.
     * 
     * @param name
     *            The human-readable name for the location
     * @param longitude
     *            GPS longitude for the location
     * @param latitude
     *            GPS latitude for the location
     */
    private EventPlace(String name, String longitude, String latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * The human-readable name for the location.
     */
    public String getName() {
        return name;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    /**
     * Parses a JSONObject into an EventPlace instance.
     * 
     * @param eventPlaceJSON
     *            The {@link JSONObject} to be parsed.
     * @return A brand-spanking new {@link EventPlace} instance.
     * @throws IllegalArgumentException
     *             if the {@link JSONObject} is malformed.
     */
    public static EventPlace fromJSON(JSONObject eventPlaceJSON)
            throws IllegalArgumentException {
        Support.checkNotNull(eventPlaceJSON);
        try {
            String name = eventPlaceJSON.getString("name");
            String latitude = eventPlaceJSON.getString("latitude");
            String longitude = eventPlaceJSON.getString("longitude");
            
            return new EventPlace(name, longitude, latitude);
        } catch (JSONException e) {
            Log.d(Support.DEBUG + "_json",
                    "JSONException thrown, most likely from malformed JSON returned");
            e.printStackTrace();
        }
        return null;
    }

}

package org.isfit.festival.programme.model;

import org.json.JSONException;
import org.json.JSONObject;

public class EventDate {
    
    private final boolean allFestival;
    private final String startAt, endAt; // Perhaps use some kind of date representation here?

    /**
     * Creates a new EventDate instance.
     * @param allFestival
     * @param startAt 
     * @param endAt
     */
    private EventDate(boolean allFestival, String startAt, String endAt) {
        this.allFestival = allFestival;
        this.startAt = startAt;
        this.endAt = endAt;
    }



    public static EventDate fromJSON(JSONObject eventDataJSON) throws JSONException {
        // TODO parse JSONObject.
        // all_festival is most likely null, make this false if it is!
        
        boolean allFestival = eventDataJSON.getBoolean("all_festival");
        String startAt = eventDataJSON.getString("start_at");
        String endAt = eventDataJSON.getString("end_at");
        
        return new EventDate(allFestival, startAt, endAt);
    }
}

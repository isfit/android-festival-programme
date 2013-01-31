package org.isfit.festival.programme.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.util.Log;

public enum EventType {
    PERFORMING_ARTS(Color.rgb(4, 129, 185)), 
    PLENARY_SESSION(Color.rgb(251, 188, 58)), 
    CONCERT(Color.rgb(56, 192, 240)),
    ART(Color.rgb(34, 54, 102)),
    CEREMONY(Color.rgb(216, 52, 21)),
    OTHER(Color.rgb(34, 34, 34));
    
    private int color;
    
    private EventType(int color) {
        this.color = color;
    }
    
    public int getColor() {
        return color;
    }
    
    @Override
    public String toString() {
        return this.name().replace('_', ' ').toLowerCase();
    }

    /**
     * Helps parse the JSON to give the correct {@link EventType}.
     * 
     * @param eventTypeJSON
     * @return
     * @throws JSONException
     */
    public static EventType fromJSON(JSONObject eventTypeJSON)
            throws JSONException {
        // TODO translate from id to enum.
        EventType eventType = null;
        int id = eventTypeJSON.getInt("id");
        switch (id) {
        case 1:
            eventType = EventType.CONCERT;
            break;
        case 2:
            eventType = EventType.PERFORMING_ARTS;
            break;
        case 3:
            eventType = EventType.ART;
            break;
        case 4:
            eventType = EventType.CEREMONY;
            break;
        case 5:
            eventType = EventType.PLENARY_SESSION;
            break;
        case 7:
            eventType = eventType.OTHER;
            break;
        default:
            Log.d(Support.DEBUG, "No matching EventType found. EventType was: "
                    + id);
            eventType = eventType.OTHER;
            break;
        }
        return eventType;
    }
}

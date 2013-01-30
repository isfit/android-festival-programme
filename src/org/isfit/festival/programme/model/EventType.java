package org.isfit.festival.programme.model;

import org.json.JSONException;
import org.json.JSONObject;

public enum EventType {
    PERFORMING_ARTS, PLENARY_SESSION, CONCERT, ART, CEREMONY, OTHER;
    
    /**
     * Helps parse the JSON to give the correct {@link EventType}.
     * @param eventTypeJSON
     * @return
     * @throws JSONException 
     */
    public static EventType fromJSON(JSONObject eventTypeJSON) throws JSONException {
        // TODO translate from id to enum.
        EventType eventType = null;
        switch (eventTypeJSON.getInt("id")) {
        case 2:
            eventType = EventType.PERFORMING_ARTS;
            break;

        default:
            break;
        }
        return eventType;
    }
}

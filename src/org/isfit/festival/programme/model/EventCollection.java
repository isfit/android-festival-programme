package org.isfit.festival.programme.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.isfit.festival.programme.EventDateHeaderItem;
import org.isfit.festival.programme.EventListItem;
import org.isfit.festival.programme.FestivalProgramme;
import org.isfit.festival.programme.R;
import org.isfit.festival.programme.util.OnTaskCompleted;
import org.isfit.festival.programme.util.Support;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

/**
 * A collection of {@link Event}s
 * <p>
 * (dagingaa):
 * Instead of having an EventsMap as a static final thingy, we should have this
 * as a singleton-thingy. We really don't need several {@link EventCollection}s
 * I think.
 * </p>
 */
public class EventCollection {
    public static final SparseArray<Event> EVENTS_MAP = new SparseArray<Event>();
    
    private List<Event> events;
    private Context context;
    private OnTaskCompleted listener;

    public EventCollection(Context context, OnTaskCompleted listener) {
        this.context = context;
        this.listener = listener;
        // Calls update to populate list upon creation.
        this.update();
    }
    
    public List<Event> getEvents() {
        return this.events;
    }

    /**
     * Updates the list of events. This will check for an active internet
     * connection and the presence of the cache. It will then fetch the cache
     * and download a new copy if it has internet access.
     * 
     * (dagingaa) This check should be done in EventsFetcher later.
     */
    public void update() {
        if (FestivalProgramme.getInstance().isConnected() &&
                (FestivalProgramme.eventsJSON == null || FestivalProgramme.eventsJSON.equals(""))) {
            EventsDownloader downloader = new EventsDownloader();
            downloader.execute("");            
        } else {
            EventsFetcher fetcher = new EventsFetcher();
            fetcher.execute("");
            // Update if we are on a network and wifi
            if (FestivalProgramme.getInstance().isConnected() && FestivalProgramme.getInstance().isWifi()) {
                EventsDownloader downloader = new EventsDownloader();
                downloader.execute("");
            }
        }

    }
    
    /**
     * Takes a JSONArray of events and parses it into POJOs
     * @param array a JSONArray containing events as JSON
     */
    private void updateEventsFromJSON(JSONArray array) {
        Support.checkNotNull(array);
        List<Event> events = new ArrayList<Event>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject eventJSON = array.getJSONObject(i);
                Event event = Event.fromJSON(eventJSON);
                events.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.events = events;
        populateEventsMap();
        Log.d(Support.DEBUG, "Events populated: " + getEvents().size());
        listener.onTaskCompleted();
    }
    
    private void populateEventsMap() {
        EVENTS_MAP.clear();
        for (Event event : events) {
            EVENTS_MAP.put(event.getId(), event);
        }
    }
    
    /**
     * Returns sorted events with groupings and headers for our poor listview to render. This is mostly a hack.
     * @param events Events to be sorted
     * @return A sorted grouping of events with nice headers for seperation. EventListItem is an abstraction for all of this.
     */
    public List<EventListItem> getSortedEventListItems() {
        Support.checkNotNull(events);
        List<EventListItem> sortedEventListItems = new ArrayList<EventListItem>();
        
        // Yes... We need to sort it, we use buckets! buckeeeeet.
        // Each bucket contains all events from that day. We use the human-readable string for this (convenience)
        // A special case must be made for "All festival"
        
        SortedMap<FestivalDay, List<Event>> bucketEvents = new TreeMap<FestivalDay, List<Event>>();
        for (Event event : events) {
            FestivalDay festivalDay = new FestivalDay(event.getFestivalDay());
            if (bucketEvents.containsKey(festivalDay)) {
                bucketEvents.get(festivalDay).add(event);
            } 
            else {
                bucketEvents.put(festivalDay, new ArrayList<Event>());
                bucketEvents.get(festivalDay).add(event);
            }
        }
        
        // This will fail, and sort the dates pretty much wrong. Hopefully the ids are sorted by day.
        // We can do this better server-side anyways, we are only dependent on that the first instance of
        // a day is in order.
        Set<Entry<FestivalDay, List<Event>>> entrySet = bucketEvents.entrySet();
        for (Entry<FestivalDay, List<Event>> entry : entrySet) {
            sortedEventListItems.add(new EventDateHeaderItem(entry.getKey()));
            sortedEventListItems.addAll(entry.getValue());
        }
        
        return sortedEventListItems;
        
    }
    
    /**
     * Fetches events from cache. 
     * 
     *  dagingaa: We should combine the two classes below into their own class responsible for fetching eventsJSON
     */
    private class EventsFetcher extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            Log.d(Support.DEBUG, "Cache hit: " + FestivalProgramme.eventsJSON.length());
            JSONArray array = null;
            try {
                array = new JSONArray(FestivalProgramme.eventsJSON);
                Log.d(Support.DEBUG, "JSONArray cache hit: " + array.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return array;
        }
         @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            updateEventsFromJSON(result);
        }
    }

    private class EventsDownloader extends
            AsyncTask<String, String, JSONArray> {

        private OnTaskCompleted listener;

        @Override
        protected JSONArray doInBackground(String... params) {
            return getJSONFromServer();
        }

        private JSONArray getJSONFromServer() {
            JSONArray array = null;
            URL url;
            InputStream stream = null;
            try {
                url = new URL(context.getResources().getString(R.string.events_url));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("Accept", "application/json");
                conn.connect();
                
                int responseCode = conn.getResponseCode();
                
                // Validate successful response
                if (responseCode/100 != 2) {
                    Log.d(Support.DEBUG + "_server", "Response code was: " + responseCode);
                    // TODO perhaps throw something nice here?
                    return null;
                }
                
                // At this point, we are guaranteed to have some kind of successful response
                
                stream = conn.getInputStream();
                
                String response = Support.stringify(stream).trim();
                 
                // Write file to cache
                FestivalProgramme.getInstance().writetoEventCache(response);
                
                array = new JSONArray(response);
                
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                // Yeah, something is terribly wrong if this exception actually happens.
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(Support.DEBUG, 
                        "Could not connect, or something else went wrong when talking to the server");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d(Support.DEBUG + "_json", 
                        "Something went wrong with parsing the JSON from string.");
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        // AAAGH ABORT ABORT ABORT
                        e.printStackTrace();
                    }
                }
            }
            return array;
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            super.onPostExecute(array);
            updateEventsFromJSON(array);
            Toast.makeText(context, "Events have been updated", Toast.LENGTH_SHORT).show();
        }

    }
}

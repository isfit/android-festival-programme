package org.isfit.festival.programme.model;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.Toast;

public class EventCollection {
    public static final Map<Integer, Event> EVENTS_MAP = new HashMap<Integer, Event>();
    
    private List<Event> events;
    private Context context;
    private OnTaskCompleted listener;

    public EventCollection(Context context, OnTaskCompleted listener) {
        // Calls update to populate list upon creation.
        this.context = context;
        this.listener = listener;
        this.update();
    }

    /**
     * Updates the list of events with fresh data
     */
    public void update() {
        // TODO check active internet connection here. Fall back to local copy if not.
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
                
                String response = stringify(stream).trim();
                
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

        /**
         * Stringifies an InputStream so we can handle it easier.
         * 
         * @param stream
         *            The {@link InputStream} to stringify
         * @return A stringified JSON response.
         * @throws IOException
         * @throws UnsupportedEncodingException
         */
        public String stringify(InputStream stream) throws IOException,
                UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            super.onPostExecute(array);
            updateEventsFromJSON(array);
            Toast.makeText(context, "Events have been updated", Toast.LENGTH_SHORT).show();
        }

    }

    public List<Event> getEvents() {
        return this.events;
    }

}

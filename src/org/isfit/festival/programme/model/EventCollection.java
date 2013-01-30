package org.isfit.festival.programme.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.isfit.festival.programme.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.util.Log;

public class EventCollection {
    private List<Event> events;
    private Context context;

    public EventCollection(Context context) {
        // Calls update to populate list upon creation.
        this.context = context;
        this.update();
    }

    /**
     * Updates the list of events with fresh data
     */
    public void update() {
        // TODO check active internet connection here. Fall back to local copy if not.
        EventsDownloader downloader = new EventsDownloader();
        downloader.execute("");

    }
    
    private void updateEventsFromJSON(JSONArray array) {
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
                conn.setDoInput(true);
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
                
                // Just do a double-take to verify that the stream is in fact open
                Support.checkNotNull(stream);
                
                String response = stringify(stream);
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
        }

    }

}

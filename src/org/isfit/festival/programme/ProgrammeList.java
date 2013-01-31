package org.isfit.festival.programme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.isfit.festival.programme.model.Event;
import org.isfit.festival.programme.model.EventCollection;
import org.isfit.festival.programme.model.FestivalDay;
import org.isfit.festival.programme.model.OnTaskCompleted;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class ProgrammeList extends Activity implements OnTaskCompleted {
    
    private EventCollection eventCollection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programme_list);
        
        eventCollection = new EventCollection(getApplicationContext(), this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_programme_list, menu);
        return true;
    }

    @Override
    public void onTaskCompleted() {
        // TODO update view
        ListView listView = (ListView) findViewById(R.id.eventListView);
        List<EventListItem> events = getSortedEventListItems(eventCollection.getEvents());
        EventListViewAdapter eventListViewAdapter = new EventListViewAdapter(
                this,
                R.layout.listview_event_row,
                events);
        listView.setAdapter(eventListViewAdapter);
    }
    
    /**
     * Returns sorted events with groupings and headers for our poor listview to render. This is mostly a hack.
     * @param events Events to be sorted
     * @return A sorted grouping of events with nice headers for seperation. EventListItem is an abstraction for all of this.
     */
    public List<EventListItem> getSortedEventListItems(List<Event> events) {
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
}

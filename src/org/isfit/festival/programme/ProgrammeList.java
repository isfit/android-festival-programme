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
import org.isfit.festival.programme.util.OnTaskCompleted;
import org.isfit.festival.programme.util.Support;

import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class ProgrammeList extends Activity implements OnTaskCompleted {
    
    private EventCollection eventCollection;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programme_list);
        
     // Check network if eventsJSON == null
        if (FestivalProgramme.eventsJSON == null && !FestivalProgramme.getInstance().isConnected()) {
            Log.d(Support.DEBUG, "No cache and no network. Launch Toast!");
            Intent intent = new Intent(this, NoNetworkActivity.class);
            startActivity(intent);
            return;
        }
        this.listView = (ListView) findViewById(R.id.eventListView);
        setOnScrollListener(false, false);
        
        eventCollection = new EventCollection(getApplicationContext(), this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onTaskCompleted() {
        List<EventListItem> events = eventCollection.getSortedEventListItems();
        EventListViewAdapter eventListViewAdapter = new EventListViewAdapter(
                this,
                R.layout.listview_event_row,
                events);
        listView.setAdapter(eventListViewAdapter);
    }
    
    private void setOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        PauseOnScrollListener listener = new PauseOnScrollListener(pauseOnScroll, pauseOnFling);
        listView.setOnScrollListener(listener);
    }
    
    
}

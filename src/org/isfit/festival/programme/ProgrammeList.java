package org.isfit.festival.programme;

import java.util.List;
import org.isfit.festival.programme.model.EventCollection;
import org.isfit.festival.programme.util.OnTaskCompleted;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
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
            launchDialog();
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
    
    private void launchDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.no_network)
        .setTitle("No network available");
        
        alertDialogBuilder.setNeutralButton("Ok, I will enable network", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        
    }
    
    
}

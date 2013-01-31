package org.isfit.festival.programme;

import org.isfit.festival.programme.model.Event;
import org.isfit.festival.programme.model.EventCollection;
import org.isfit.festival.programme.model.OnTaskCompleted;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends Activity {

    public static final String ARG_EVENT_ID = "event_id";
    
    private Event event;
    private ImageView eventImageBig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        if (getIntent().getExtras().containsKey(ARG_EVENT_ID)) {
            int id = (Integer) getIntent().getExtras().get(ARG_EVENT_ID);
            event = EventCollection.EVENTS_MAP.get(id);
            findViewById(R.id.eventDescription).setBackgroundColor(event.getEventType().getColor());
            eventImageBig = (ImageView) findViewById(R.id.eventImageBig);
            eventImageBig.setImageBitmap(event.getImageBitmap(eventImageBig));
            ((TextView) findViewById(R.id.eventDateHuman)).setText(event.getFestivalDay());
            ((TextView) findViewById(R.id.eventTitleBig)).setText(event.getTitle());
            TextView eventBody = ((TextView) findViewById(R.id.eventBody));
            eventBody.setText(Html.fromHtml(event.getBodyAsHTML()));
            eventBody.setMovementMethod(new ScrollingMovementMethod());
            
            this.setTitle(event.getTitle());
            
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}

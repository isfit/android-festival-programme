package org.isfit.festival.programme;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.isfit.festival.programme.model.Event;
import org.isfit.festival.programme.model.EventCollection;
import org.isfit.festival.programme.util.OnTaskCompleted;
import org.isfit.festival.programme.util.Support;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends Activity {

    public static final String ARG_EVENT_ID = "event_id";

    private Event event;
    private ImageView eventImageBig;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Typeface tfBold = Typeface.createFromAsset(getBaseContext().getAssets(),
                "fonts/Apertura-Bold.otf");
        Typeface tfMedium = Typeface.createFromAsset(getBaseContext().getAssets(),
                "fonts/Apertura-MediumCondensed.otf");
        
        if (getIntent().getExtras().containsKey(ARG_EVENT_ID)) {
            int id = (Integer) getIntent().getExtras().get(ARG_EVENT_ID);
            event = EventCollection.EVENTS_MAP.get(id);
            findViewById(R.id.eventDescription).setBackgroundColor(
                    event.getEventType().getColor());
            eventImageBig = (ImageView) findViewById(R.id.eventImageBig);
            event.loadImageBitmap(eventImageBig);
            TextView eventDateHuman = ((TextView) findViewById(R.id.eventDateHuman));
            eventDateHuman.setText(event.getFestivalDay());

            TextView eventTitleBig = ((TextView) findViewById(R.id.eventTitleBig));
            eventTitleBig.setText(event.getTitle());
            eventTitleBig.setTypeface(tfBold);

            TextView eventTypetext = ((TextView) findViewById(R.id.eventTypeText));
            eventTypetext.setText(event.getEventType().toString());
            
            if (!event.isAllFestival()) {
                // This is why I hate java...
                Calendar date;
                try {
                    date = Support.toCalendar(event.getEventTime());
                    int minute = date.get(Calendar.MINUTE);
                    String actualMinute = "" + minute;
                    if (minute < 10) {
                        actualMinute = "0" + minute;
                    }
                    ((TextView) findViewById(R.id.eventTimeFromTo))
                            .setText(date.get(Calendar.HOUR_OF_DAY) + ":"
                                    + actualMinute);
                } catch (ParseException e) {
                    // TODO Auto-generated catch b lock
                    e.printStackTrace();
                }
            }

            ((TextView) findViewById(R.id.EventPriceText)).setText(event
                    .getPriceMember() + ",-/" + event.getPriceOther() + ",-");
            ((TextView) findViewById(R.id.eventPlaceText)).setText(event
                    .getEventPlace().getName());

            TextView eventBody = ((TextView) findViewById(R.id.eventBody));
            eventBody.setText(Html.fromHtml(event.getBodyAsHTML()));
            eventBody.setTypeface(tfMedium);
            
            this.setTitle(event.getTitle());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
    
    @Override
    public void onBackPressed() 
    {
        this.finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
        return;
    }

}

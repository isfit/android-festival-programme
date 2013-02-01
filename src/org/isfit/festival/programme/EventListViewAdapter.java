package org.isfit.festival.programme;

import java.util.List;

import org.isfit.festival.programme.model.Event;
import org.isfit.festival.programme.util.OnTaskCompleted;
import org.isfit.festival.programme.util.Support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EventListViewAdapter extends ArrayAdapter<EventListItem> implements OnClickListener {
    
    private int layoutResourceId;
    private List<EventListItem> events;
    private ListView list;

    public EventListViewAdapter(Context context, int layoutResourceId,
            List<EventListItem> events) {
        super(context, layoutResourceId, events);
        this.layoutResourceId = layoutResourceId;
        this.events = events;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EventHolder holder = null;
        EventListItem eventListItem = events.get(position);
        list = (ListView) parent;
        
        if (row != null && 
                !eventListItem.isDateHeader() && 
                ((EventHolder)row.getTag()).event == (Event) eventListItem) {
            return convertView;
        }
        
        if (eventListItem.isDateHeader()) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.listview_date_header_row, parent, false);
            
            holder = new EventHolder();
            holder.eventTitle = (TextView)row.findViewById(R.id.humanReadableDateHeader);
            
            row.setTag(holder);

            holder.eventTitle.setText(((EventDateHeaderItem) eventListItem).getFestivalDay());
            holder.eventTitle.setClickable(false);
        } else {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new EventHolder();
            holder.frontImage = (ImageView)row.findViewById(R.id.eventImage);
            holder.eventTitle = (TextView)row.findViewById(R.id.eventTitle);
            holder.event = (Event) eventListItem;
            
            row.setTag(holder);
            
            holder.eventTitle.setText(((Event) eventListItem).getTitle());
            ((Event) eventListItem).loadImageBitmap(holder.frontImage);
            
            row.setOnClickListener(this);
            row.setBackgroundColor(holder.event.getEventType().getColor());
            
        }
        return row;
        
    }
    
    static class EventHolder {
        Event event;
        ImageView frontImage;
        TextView eventTitle;
    }

    @Override
    public void onClick(View v) {
        Event event = ((EventHolder)v.getTag()).event;
        Log.d(Support.DEBUG, "Event clicked: " + event.getTitle());
        Intent eventIntent = new Intent(getContext(), EventActivity.class);
        eventIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        eventIntent.putExtra(EventActivity.ARG_EVENT_ID, event.getId());
        getContext().startActivity(eventIntent);
    }

}

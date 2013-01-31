package org.isfit.festival.programme;

import java.util.List;

import org.isfit.festival.programme.model.Event;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventListViewAdapter extends ArrayAdapter<EventListItem> {
    
    private int layoutResourceId;
    private List<EventListItem> events;

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
        
        if (eventListItem.isDateHeader()) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.listview_date_header_row, parent, false);
            
            holder = new EventHolder();
            holder.eventTitle = (TextView)row.findViewById(R.id.humanReadableDateHeader);
            
            row.setTag(holder);

            holder.eventTitle.setText(((EventDateHeaderItem) eventListItem).getFestivalDay());
            holder.eventTitle.setClickable(false);
            holder.eventTitle.setTextColor(Color.RED);
        } else {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new EventHolder();
            holder.frontImage = (ImageView)row.findViewById(R.id.eventImage);
            holder.eventTitle = (TextView)row.findViewById(R.id.eventTitle);
            
            row.setTag(holder);
            
            holder.eventTitle.setText(((Event) eventListItem).getTitle());
            holder.frontImage.setImageBitmap(((Event) eventListItem).getImageBitmap());
            
        }
        return row;
        
    }
    
    static class EventHolder {
        ImageView frontImage;
        TextView eventTitle;
    }

}

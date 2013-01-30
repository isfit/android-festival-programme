package org.isfit.festival.programme;

import org.isfit.festival.programme.model.EventCollection;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ProgrammeList extends Activity {
    
    private EventCollection eventCollection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programme_list);
        
        eventCollection = new EventCollection(getApplicationContext());
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_programme_list, menu);
        return true;
    }
}

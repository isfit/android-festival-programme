package org.isfit.festival.programme;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NoNetworkActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
        Button button = (Button) findViewById(R.id.relaunch);
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                relaunch();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_no_network, menu);
        return false;
    }
    
    private void relaunch() {
        Intent intent = new Intent(this, ProgrammeList.class);
        startActivity(intent);
    }
}

package de.noucake.tubecompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private TubeCompanion main;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = TubeCompanion.getInstance(this);

        inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        LinearLayout content = findViewById(R.id.dashboard_content);
        content.addView(inflater.inflate(R.layout.dasboard_item, null, false));
        content.addView(inflater.inflate(R.layout.dasboard_item, null, false));
        content.addView(inflater.inflate(R.layout.dasboard_item, null, false));
    }
}

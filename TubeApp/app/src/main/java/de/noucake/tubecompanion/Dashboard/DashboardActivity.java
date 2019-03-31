package de.noucake.tubecompanion.Dashboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import de.noucake.tubecompanion.R;
import de.noucake.tubecompanion.TubeCompanion;

public class DashboardActivity extends AppCompatActivity {

    private TubeCompanion main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = TubeCompanion.getInstance(this);
    }

}

package de.noucake.tubecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import de.noucake.tubecompanion.Dashboard.DashboardActivity;
import de.noucake.tubecompanion.Login.LoginInputActivity;

public class MainActivity extends AppCompatActivity {

    private TubeCompanion main;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = TubeCompanion.getInstance(this);
        main.init();

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //main.onDestory();

        //TODO
        //main.onDetroid
        //Yes im funny.
    }
}

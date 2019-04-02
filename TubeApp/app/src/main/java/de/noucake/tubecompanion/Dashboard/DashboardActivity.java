package de.noucake.tubecompanion.Dashboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Random;

import de.noucake.tubecompanion.Data.TubeData;
import de.noucake.tubecompanion.R;
import de.noucake.tubecompanion.TubeCompanion;

public class DashboardActivity extends AppCompatActivity {

    private TubeCompanion main;
    private DashboardView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        main = TubeCompanion.getInstance(this);
        view = new DashboardView(this);

        addDummyData();
    }

    private void addDummyData(){
        TubeData d = new TubeData("1234", "Hello", "World!");
        d.setComplete(true);
        view.addTubeData(d);
        d = new TubeData("1235", "Helly", "Belly!");
        d.setComplete(true);
        view.addTubeData(d);
        d = new TubeData("1236", "Hsdaaselly", "Bdadaselly!");
        d.setComplete(false);
        view.addTubeData(d);
        d = new TubeData("1237", "Hsdaaselly", "Bdadaselly!");
        d.setComplete(true);
        view.addTubeData(d);
        d = new TubeData("4237", "Hsdaaselly", "Bdadaselly!");
        d.setComplete(true);
        view.addTubeData(d);
        d = new TubeData("4237", "Hsdaaselly", "Bdadaselly!");
        d.setComplete(true);
        view.addTubeData(d);
        d = new TubeData("4237", "Hsdaaselly", "Bdadaselly!");
        d.setComplete(true);
        view.addTubeData(d);
        d = new TubeData("4237", "Hsdaaselly", "Bdadaselly!");
        d.setComplete(true);
        view.addTubeData(d);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.dashboard_additem:
                Random r = new Random();
                String id = r.nextFloat()+"";
                view.addTubeData(new TubeData(id, "Random","Generator"));
                break;
            case R.id.dashboard_removeall:
                view.removeAll();
                break;
            case R.id.dashboard_showlogin:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
    }



    public DashboardView getView() {
        return view;
    }
}

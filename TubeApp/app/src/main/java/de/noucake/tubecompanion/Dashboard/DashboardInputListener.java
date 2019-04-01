package de.noucake.tubecompanion.Dashboard;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import de.noucake.tubecompanion.R;

public class DashboardInputListener implements View.OnClickListener, View.OnTouchListener {

    private DashboardActivity dashboard;

    public DashboardInputListener(DashboardActivity dashboard){
        this.dashboard = dashboard;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v.getId() == R.id.dashboard_item_root){
            v.setBackgroundColor(Color.LTGRAY);
        }

        return false;
    }
}

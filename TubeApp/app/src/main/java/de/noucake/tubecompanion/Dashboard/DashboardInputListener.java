package de.noucake.tubecompanion.Dashboard;

import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import de.noucake.tubecompanion.R;

public class DashboardInputListener implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {

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
            return onDashboardItemTouched(dashboard.getView().getItemByView(v), event);
        }

        return false;
    }



    private boolean onDashboardItemTouched(DashboardItem item, MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //item.setSelected();
                return true;
            case MotionEvent.ACTION_UP:
                item.setUnselected();
                return false;
        }
        return true;
    }

    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.dashboard_item_root){
            dashboard.getView().getItemByView(v).setSelected();
        }
        return false;
    }
}

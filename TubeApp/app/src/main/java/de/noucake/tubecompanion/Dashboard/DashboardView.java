package de.noucake.tubecompanion.Dashboard;

import android.view.LayoutInflater;

import de.noucake.tubecompanion.Data.TubeData;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DashboardView {

    private DashboardActivity dashboard;
    private LayoutInflater inflater;

    public DashboardView(DashboardActivity dashboard){
        this.dashboard = dashboard;

        inflater = (LayoutInflater)dashboard.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    public void addTubeData(TubeData data){

    }

}

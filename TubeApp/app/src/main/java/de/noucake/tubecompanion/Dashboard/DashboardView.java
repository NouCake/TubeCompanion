package de.noucake.tubecompanion.Dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import de.noucake.tubecompanion.Data.TubeData;
import de.noucake.tubecompanion.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DashboardView {

    private DashboardActivity dashboard;
    private LayoutInflater inflater;
    private ViewGroup contentParent;
    private DashboardHandler handler;
    private DashboardInputListener dashboardInput;

    private List<DashboardItem> items;

    public static Bitmap defaultPicture;

    public DashboardView(DashboardActivity dashboard){
        this.dashboard = dashboard;

        handler = new DashboardHandler(dashboard);
        items = new LinkedList<>();
        inflater = (LayoutInflater)dashboard.getSystemService(LAYOUT_INFLATER_SERVICE);
        contentParent = dashboard.findViewById(R.id.dashboard_content);
        dashboardInput = new DashboardInputListener(dashboard);

        loadDefaultPicture();
    }

    private void loadDefaultPicture(){
        defaultPicture = BitmapFactory.decodeResource(dashboard.getResources(), R.drawable.loading);
    }

    private void addDahboardItem(TubeData data){
        ViewGroup v = (ViewGroup)inflater.inflate(R.layout.dasboard_item, null, false);
        dashboard.registerForContextMenu(v);
        v.setOnCreateContextMenuListener(dashboardInput);

        contentParent.addView(v);
        items.add(new DashboardItem(data, v));
    }

    /**
     * Should only be called by (Dashboard)Handler
     * @param data
     */
    void addTubeDataAsHandler(TubeData data){
        addDahboardItem(data);
    }

    /**
     * Should only be called by (Dashboard)Handler
     * @param data
     */
    void removeTubeDataAsHandler(TubeData data){
        DashboardItem item = getItemByTubeData(data);
        contentParent.removeView(item.getItemRoot());
        items.remove(item);
    }

    /**
     * Should only be called by (Dashboard)Handler
     * @param item
     */
    void removeItemAsHandler(DashboardItem item){
        contentParent.removeView(item.getItemRoot());
        items.remove(item);
    }

    /**
     * Should only be called by (Dashboard)Handler
     */
    void removeAllAsHandler(){
        for(DashboardItem item : items){
            contentParent.removeView(item.getItemRoot());
        }
    }

    void removeItem(DashboardItem item){
        handler.enqueueRemove(item);
    }

    private DashboardItem getItemByTubeData(TubeData data){
        for(DashboardItem i : items){
            if(i.getData().getId().equals(data.getId())){
                return i;
            }
        }
        Log.d("TubeCompanion-D", "Could not find Item in View");
        return null;
    }

    public DashboardItem getItemByView(View v){
        for(DashboardItem i : items){
            if(i.getItemRoot() == v){
                return i;
            }
        }
        return null;
    }

    public void addTubeData(TubeData data){
        handler.enqueueAdd(data);
    }

    public void removeTubeData(TubeData data){
        handler.enqueueRemove(data);
    }

    public void removeAll() {
        handler.enqueueRemoveAll();
    }

}

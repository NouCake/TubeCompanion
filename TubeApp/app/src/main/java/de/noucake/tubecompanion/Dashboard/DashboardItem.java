package de.noucake.tubecompanion.Dashboard;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.noucake.tubecompanion.Data.TubeData;
import de.noucake.tubecompanion.R;

public class DashboardItem {

    private ViewGroup itemRoot;
    private TubeData data;

    private ImageView image;
    private TextView title;
    private TextView author;
    private ProgressBar prog;

    public DashboardItem(TubeData data, ViewGroup root){
        this.data = data;
        this.itemRoot = root;

        initViewContent();
        updateViewData();
    }

    private void updateViewData(){
        title.setText("Hello");
        author.setText("World!");
        prog.setProgress(80);
        image.setImageAlpha(255);
    }

    private void initViewContent(){
        int elements = itemRoot.getChildCount();
        for(int i = 0; i < elements; i++){
            registerView(itemRoot.getChildAt(i));
        }
    }

    private void registerView(View v){
        switch(v.getId()){
            case R.id.dashboard_item_title:
                title = (TextView)v;
                break;
            case R.id.dashboard_item_author:
                author = (TextView)v;
                break;
            case R.id.dashboard_item_image:
                image = (ImageView)v;
                break;
            case R.id.dashboard_item_progressbar:
                prog = (ProgressBar)v;
                break;
        }
    }

}

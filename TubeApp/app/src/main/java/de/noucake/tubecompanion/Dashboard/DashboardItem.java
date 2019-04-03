package de.noucake.tubecompanion.Dashboard;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.noucake.tubecompanion.Data.TubeData;
import de.noucake.tubecompanion.R;

public class DashboardItem {

    private static final int colorIncomplete = 0xFFF0F0F0;
    private static final int colorComplete = 0xFFFAFAFA;

    private ViewGroup itemRoot;
    private TubeData data;

    private ImageView image;
    private TextView id;
    private TextView title;
    private TextView author;
    private ProgressBar prog;

    public DashboardItem(TubeData data, ViewGroup root){
        this.data = data;
        this.itemRoot = root;

        initViewContent();
        updateViewData();
    }

    public void updateViewData(){
        id.setText(data.getId());
        title.setText(data.getTitle());
        author.setText(data.getAuthor());
        prog.setProgress(data.getDownloadProgress());
        image.setImageBitmap(DashboardView.defaultPicture);
        if(data.isComplete()){
            itemRoot.setBackgroundColor(colorComplete);
        } else {
            itemRoot.setBackgroundColor(colorIncomplete);
        }
    }

    private void initViewContent(){
        int elements = itemRoot.getChildCount();
        for(int i = 0; i < elements; i++){
            registerView(itemRoot.getChildAt(i));
        }
    }

    public void setSelected(){
        itemRoot.setBackgroundColor(colorIncomplete);
    }

    public void setUnselected(){
        itemRoot.setBackgroundColor(colorComplete);
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
            case R.id.dashboard_item_id:
                id = (TextView)v;
        }
    }

    public TubeData getData() {
        return data;
    }

    public ViewGroup getItemRoot() {
        return itemRoot;
    }
}

package de.noucake.tubecompanion.Dashboard;

import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import de.noucake.tubecompanion.R;

public class DashboardInputListener implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener, View.OnClickListener {

    private DashboardActivity dashboard;
    private DashboardItem lastSelectedItem;
    private AlertDialog renameDialog;
    private AlertDialog.Builder renameDialogBuilder;
    private EditText renameDialogInputTitle;
    private EditText renameDialogInputAuthor;

    public DashboardInputListener(DashboardActivity dashboard){
        this.dashboard = dashboard;

        initRenameDialog();
    }

    private void initRenameDialog(){
        renameDialogBuilder = new AlertDialog.Builder(dashboard);
        View v = dashboard.getLayoutInflater().inflate(R.layout.dashboard_layout_rename, null);
        renameDialogInputTitle = v.findViewById(R.id.dashboard_rename_title);
        renameDialogInputAuthor = v.findViewById(R.id.dashboard_rename_author);
        v.findViewById(R.id.dashboard_rename_ok).setOnClickListener(this);
        renameDialogBuilder.setView(v);
        renameDialog = renameDialogBuilder.create();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        lastSelectedItem = dashboard.getView().getItemByView(v); //ugly
        if(lastSelectedItem != null){
            dashboard.getMenuInflater().inflate(R.menu.dashboard_item_context, menu);
            for(int i = 0; i < menu.size(); i++){
                menu.getItem(i).setOnMenuItemClickListener(this);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard_item_context_rename:
                showRenameDialog();
                break;
            case R.id.dashboard_item_context_remove:
                //TODO
                //Ask main if deletion is ok / deligate this event to main
                dashboard.getView().removeItem(lastSelectedItem); //ugly
                break;
        }
        return false;
    }

    private void showRenameDialog(){
        renameDialogInputTitle.setText(lastSelectedItem.getData().getTitle());
        renameDialogInputAuthor.setText(lastSelectedItem.getData().getAuthor());
        renameDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dashboard_rename_ok:
                lastSelectedItem.getData().setTitle(renameDialogInputTitle.getText().toString());
                lastSelectedItem.getData().setAuthor(renameDialogInputAuthor.getText().toString());
                lastSelectedItem.updateViewData();
                renameDialog.hide();
                break;
            case R.id.dashboard_rename_cancel:
                renameDialog.hide();
                break;
        }
    }
}

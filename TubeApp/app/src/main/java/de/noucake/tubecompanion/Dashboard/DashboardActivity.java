package de.noucake.tubecompanion.Dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Random;

import de.noucake.tubecompanion.Data.DataLoader;
import de.noucake.tubecompanion.Data.TubeData;
import de.noucake.tubecompanion.R;
import de.noucake.tubecompanion.Server.TubeServer;
import de.noucake.tubecompanion.Server.TubeTypes;
import de.noucake.tubecompanion.TubeCompanion;

public class DashboardActivity extends AppCompatActivity {

    private TubeCompanion main;
    private DashboardView view;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        main = TubeCompanion.getInstance(this);
        view = new DashboardView(this);
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.dashboard_removeall:
                view.removeAll();
                break;
            case R.id.dashboard_host:
                //TODO: CHANGE
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Host Adress");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                input.setText(main.getServer().getHostAdress());
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataLoader.saveHostAdress(main.getMainActivity(), input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            case R.id.dashboard_showlogin:
                //TODO: IMPLEMENT
                main.showLogin();
                break;
            case R.id.dashboard_pending:
                //main.getServer().sendPendingRequestsRequest();
                main.getServer().sendFileRequest(new TubeData("3xl2sAypyMg"), TubeTypes.FILE_IMAGE);
        }

        return super.onOptionsItemSelected(item);
    }

    public DashboardView getView() {
        return view;
    }

}

package de.noucake.tubecompanion;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import de.noucake.tubecompanion.Dashboard.DashboardActivity;
import de.noucake.tubecompanion.Data.DataLoader;
import de.noucake.tubecompanion.Login.LoginInputActivity;
import de.noucake.tubecompanion.Server.TubeServerHandler;

public class TubeCompanion {

    private static TubeCompanion singleton;
    public static TubeCompanion getInstance(Activity activity){
        if(singleton == null){
            singleton = new TubeCompanion();
        }
        singleton.registerActivity(activity);
        return singleton;
    }

    private TubeServerHandler server;

    private MainActivity mainActivity;
    private LoginInputActivity loginActivity;
    private DashboardActivity dashboardActivity;

    private TubeHandler handler;

    private TubeCompanion(){
        server = new TubeServerHandler(this);
        handler = new TubeHandler(this);

        server.connect();
    }

    private void registerActivity(Activity activity){
        if(activity instanceof MainActivity){
            mainActivity = (MainActivity)activity;
        } else if(activity instanceof LoginInputActivity){
            loginActivity = (LoginInputActivity)activity;
        } else if(activity instanceof DashboardActivity){
            dashboardActivity = (DashboardActivity)activity;
        }
    }

    public void onConnectionSucceed(){
        if(!server.isConnected()){
            Log.d("TubeCompanion-D", "Someone lied to me...");
            Toast.makeText(mainActivity, "Some error occured!", Toast.LENGTH_SHORT).show();
            mainActivity.finish();
        }

        startLogin();
    }

    public void onLoginSucceed(){
        if(loginActivity != null){
            loginActivity.finish();
            loginActivity = null;
        }
    }

    private void startLogin(){
        assert mainActivity != null;

        if(!DataLoader.hasLoginData(mainActivity)){
            String[] data = DataLoader.loadLoginData(mainActivity);

            server.login(data[0], data[1]);
        } else {
            startLoginActivity();
        }
    }

    private void startLoginActivity(){
        assert mainActivity != null;

        Intent intent = new Intent(mainActivity, LoginInputActivity.class);
        mainActivity.startActivity(intent);
    }

    public void onLoginInputReady(){
        assert  loginActivity != null;

        String[] data = loginActivity.getLoginData();
        server.login(data[0], data[1]);
    }

    public TubeHandler getHandler(){
        return handler;
    }

    public TubeServerHandler getServer() {
        return server;
    }
}

package de.noucake.tubecompanion;

import android.os.Handler;
import android.os.Message;

public class TubeHandler extends Handler {

    public static final int ON_CONNECTION = 0;
    public static final int LOGIN_INPUT_READY = 1;
    public static final int SHOW_MESSAGE = 2;
    public static final int SHOW_LOGIN = 3;

    private final TubeCompanion main;
    private String msg;

    public TubeHandler(TubeCompanion main){
        this.main = main;
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what){
            case ON_CONNECTION:
                main.onConnectionSucceed();
                break;
            case LOGIN_INPUT_READY:
                main.onLoginInputActivityReady();
                break;
            case SHOW_MESSAGE:
                main.displayMessage(this.msg);
                break;
            case SHOW_LOGIN:
                main.showLogin();
        }
    }

    public void displayMessage(String msg){
        this.msg = msg;
        sendEmptyMessage(SHOW_MESSAGE);
    }

}

package de.noucake.tubecompanion;

import android.os.Handler;
import android.os.Message;

public class TubeHandler extends Handler {

    public static final int ON_CONNECTION = 0;
    public static final int LOGIN_INPUT_READY = 1;

    private TubeCompanion main;

    TubeHandler(TubeCompanion main){
        this.main = main;
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what){
            case ON_CONNECTION:
                main.onConnectionSucceed();
                break;
            case LOGIN_INPUT_READY:
                main.onLoginInputReady();
                break;
        }
    }
}

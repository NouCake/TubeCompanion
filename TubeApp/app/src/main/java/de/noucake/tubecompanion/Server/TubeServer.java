package de.noucake.tubecompanion.Server;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import java.net.URISyntaxException;

import de.noucake.tubecompanion.Data.DataLoader;
import de.noucake.tubecompanion.Server.EventListener.ConnectListener;
import de.noucake.tubecompanion.Server.EventListener.LoginListener;
import de.noucake.tubecompanion.Server.Requests.TubeConnectionHandler;
import de.noucake.tubecompanion.TubeCompanion;
import de.noucake.tubecompanion.TubeHandler;

public class TubeServer {

    private TubeCompanion main;

    private final ConnectListener connectListener;
    private final LoginListener loginListener;
    private final TubeLoginHandler loginHandler;
    private final TubeConnectionHandler connectionHandler;

    public TubeServer(TubeCompanion main){
        this.main = main;
        loginHandler = new TubeLoginHandler(main, this);
        connectListener = new ConnectListener(this);
        loginListener = new LoginListener(this);
        connectionHandler = new TubeConnectionHandler(this);

        loadHostAdress();
    }

    private void loadHostAdress(){
        String loadedHost = DataLoader.loadHostAdress(main.getMainActivity());
        if(loadedHost != null){
            connectionHandler.setHost(loadedHost);
        }
    }

    public void connect(){
        connectionHandler.connect();
        connectionHandler.addListener(TubeConnectionHandler.EVENT_CONNECT, connectListener);
    }
    public void login(String username, String password){
        loginHandler.login(username, password);
    }
    public void sendPacketDirect(String tag, String packet){
        assert isConnected();
        connectionHandler.send(tag, packet);
    }

    public void onConnected(){
        connectionHandler.removeListener(TubeConnectionHandler.EVENT_CONNECT);
        connectionHandler.addListener(TubeConnectionHandler.EVENT_LOGIN, loginListener);
        //TODO
        //connectionHandler.addListener(TubeConnectionHandler.EVENT_RECONNECT, );
        main.getHandler().sendEmptyMessage(TubeHandler.ON_CONNECTION);
    }
    public void onConnectionFailed(int code){
        Log.d("TubeCompanion-D", "Coonection failed " + code);
    }
    public void onLoginResponse(int responseType){
        loginHandler.onLoginResponse(responseType);
    }

    public boolean isConnected(){
        return connectionHandler.isConnected();
    }
    public boolean isLoggedIn(){
        return loginHandler.isLoggedIn();
    }
    public String getHostAdress() {
        return connectionHandler.getHost();
    }
}

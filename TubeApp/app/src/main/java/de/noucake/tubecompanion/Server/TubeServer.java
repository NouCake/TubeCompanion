package de.noucake.tubecompanion.Server;

import android.util.Log;

import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import java.net.URISyntaxException;

import de.noucake.tubecompanion.Server.EventListener.ConnectListener;
import de.noucake.tubecompanion.Server.EventListener.LoginListener;
import de.noucake.tubecompanion.Server.Packets.TubePacketGenerator;
import de.noucake.tubecompanion.TubeCompanion;
import de.noucake.tubecompanion.TubeHandler;

public class TubeServer {

    private final String host = "http://192.168.178.28:12012";

    private TubeCompanion main;

    private Socket socket;
    private ConnectListener connectListener;
    private LoginListener loginListener;

    public TubeServer(TubeCompanion main){
        this.main = main;
        connectListener = new ConnectListener(this);
        loginListener = new LoginListener(this);
    }

    public void connect(){
        try {
            socket = IO.socket(host);
            socket.on("connect", connectListener);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            onConnectionFailed(0);
        }
    }

    public void onConnected(){
        socket.off("connect");

        socket.on("login", loginListener);

        main.getHandler().sendEmptyMessage(TubeHandler.ON_CONNECTION);
    }

    public void onConnectionFailed(int code){
        Log.d("TubeCompanion-D", "Coonection failed " + code);
    }

    public boolean isConnected(){
        return socket.connected();
    }

    public void login(String username, String password){
        sendPacket("login", TubePacketGenerator.generateLoginPacket(username, password));
    }

    public void onLoginResponse(int responseType){
        Log.d("TubeCompanion-D", ""+responseType);

        switch (responseType){
            case TubeTypes.LOGIN_SUCCESS:
                main.onLoginSucceed();
                break;
        }

    }

    /**
     * @param tag
     * @param packet Has to be a JSON parseable String.
     */
    private void sendPacket(String tag, String packet){
        socket.emit(tag, packet);
    }

}

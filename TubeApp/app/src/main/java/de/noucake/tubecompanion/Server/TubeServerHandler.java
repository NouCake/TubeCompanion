package de.noucake.tubecompanion.Server;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import java.net.URISyntaxException;

import de.noucake.tubecompanion.Data.DataLoader;
import de.noucake.tubecompanion.Server.EventListener.ConnectListener;
import de.noucake.tubecompanion.Server.EventListener.LoginListener;
import de.noucake.tubecompanion.TubeCompanion;
import de.noucake.tubecompanion.TubeHandler;

public class TubeServerHandler {

    private static String host = "http://192.168.178.30:12012";

    private TubeCompanion main;

    private Socket socket;
    private ConnectListener connectListener;
    private LoginListener loginListener;
    private TubeLoginHandler loginHandler;

    public TubeServerHandler(TubeCompanion main){
        this.main = main;
        loginHandler = new TubeLoginHandler(main, this);
        connectListener = new ConnectListener(this);
        loginListener = new LoginListener(this);

        String loadedHost = DataLoader.loadHostAdress(main.getMainActivity());
        if(loadedHost != null)
            host = loadedHost;
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
        socket.on("reconnect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                main.getDashboardActivity().getView().removeAll();
                socket.emit("message", "Hello");
            }
        });
        main.getHandler().sendEmptyMessage(TubeHandler.ON_CONNECTION);
    }

    public void onConnectionFailed(int code){
        Log.d("TubeCompanion-D", "Coonection failed " + code);
    }
    public void login(String username, String password){
        loginHandler.login(username, password);
    }

    public void onLoginResponse(int responseType){
        loginHandler.onLoginResponse(responseType);
    }

    /**
     *  Should only be called to send unprivileged packets/requests
     * @param tag
     * @param packet Has to be a JSON parseable String.
     */
    public void sendPacketDirect(String tag, String packet){
        assert socket.connected();

        socket.emit(tag, packet);
    }

    public boolean isConnected(){
        return socket.connected();
    }

    public static String getHostAdress() {
        return host;
    }
}

package de.noucake.tubecompanion.Server;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import de.noucake.tubecompanion.Server.TubeServer;

public class TubeConnectionHandler {

    public final static String EVENT_CONNECT = "connect";
    public final static String EVENT_RECONNECT = "reconnect";
    public final static String EVENT_DISCONNECT = "disconnect";

    public final static String EVENT_LOGIN = "login";
    public final static String EVENT_REQUEST = "request";
    public final static String EVENT_RESPONSE = "data";

    private final TubeServer server;
    private Socket socket;
    private String host = "http://192.168.178.30:12012";

    public TubeConnectionHandler(TubeServer server){
        this.server = server;
    }

    public void initConnection(){
        try {
            socket = IO.socket(host);
        } catch (URISyntaxException e) {
            server.onConnectionFailed(0);
            e.printStackTrace();
        }
    }
    public void connect(){
        socket.connect();
    }
    public void send(String event, Object data){
        socket.emit(event, data);
        Log.d("TubeCompanion-Packet", data.toString());
    }
    public void addListener(String event, Emitter.Listener listener){
        socket.on(event, listener);
    }
    public void removeListener(String event){
        socket.off(event);
    }

    public void setHost(String host) {
        this.host = host;
    }
    public String getHost() {
        return host;
    }
    public boolean isConnected(){
        return socket.connected();
    }

}

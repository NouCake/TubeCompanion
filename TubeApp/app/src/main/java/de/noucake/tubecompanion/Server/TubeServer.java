package de.noucake.tubecompanion.Server;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;

import de.noucake.tubecompanion.Data.DataLoader;
import de.noucake.tubecompanion.Data.TubeData;
import de.noucake.tubecompanion.Server.EventListener.ConnectListener;
import de.noucake.tubecompanion.Server.EventListener.ResponseListener;
import de.noucake.tubecompanion.Server.EventListener.LoginListener;
import de.noucake.tubecompanion.Server.Requests.FileRequest;
import de.noucake.tubecompanion.Server.Requests.MetaDataRequest;
import de.noucake.tubecompanion.Server.Requests.PendingRequestsRequest;
import de.noucake.tubecompanion.Server.Requests.TubeRequest;
import de.noucake.tubecompanion.TubeCompanion;
import de.noucake.tubecompanion.TubeHandler;

public class TubeServer {

    private TubeCompanion main;

    private final ConnectListener connectListener;
    private final LoginListener loginListener;
    private final ResponseListener responseListener;
    private final TubeLoginHandler loginHandler;
    private final TubeConnectionHandler connectionHandler;
    private final TubeRequestHandler requestHandler;

    public TubeServer(TubeCompanion main){
        this.main = main;

        requestHandler = new TubeRequestHandler(main);
        responseListener = new ResponseListener(main, requestHandler);

        connectListener = new ConnectListener(this);
        loginListener = new LoginListener(this);

        loginHandler = new TubeLoginHandler(main, this);
        connectionHandler = new TubeConnectionHandler(this);

        loadHostAdress();
    }

    private void loadHostAdress(){
        String loadedHost = DataLoader.loadHostAdress(main.getMainActivity());
        if(loadedHost != null){
            Log.d("TubeCompanion-D", "Setting host to " + loadedHost);
            connectionHandler.setHost(loadedHost);
        }
    }
    private void sendRequest(TubeRequest req){
        String packet = TubePacketGenerator.generateRequestPacket(req);
        connectionHandler.send(TubeConnectionHandler.EVENT_REQUEST, packet);
    }

    public void connect(){
        connectionHandler.initConnection();
        connectionHandler.addListener(TubeConnectionHandler.EVENT_CONNECT, connectListener);
        connectionHandler.addListener(TubeConnectionHandler.EVENT_RECONNECT,
                new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        loginHandler.onReconnect();
                    }
                });
        connectionHandler.addListener(TubeConnectionHandler.EVENT_DISCONNECT,
                new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        loginHandler.onDisconnect();
                    }
                });

        connectionHandler.connect();
    }
    public void login(String username, String password){
        loginHandler.login(username, password);
    }
    public void sendPendingRequestsRequest(){
        PendingRequestsRequest req = new PendingRequestsRequest(TubeRequestHandler.getNextReqID());
        requestHandler.addRequest(req);
        sendRequest(req);
    }
    /**
     * TubeData over (String)ID to gurantee TubeData exists
     * (And Probably is in DataHolder)
     * */public void sendMetaDataRequest(TubeData data){
        MetaDataRequest req = new MetaDataRequest(TubeRequestHandler.getNextReqID(), data.getId());
        requestHandler.addRequest(req);
        sendRequest(req);
    }
    public void sendFileRequest(TubeData data, int filetype){
        int reqid = TubeRequestHandler.getNextReqID();
        FileRequest req = new FileRequest(reqid, data.getId(), filetype);
        requestHandler.addRequest(req);
        sendRequest(req);
    }
    public void sendPacketDirect(String tag, String packet){
        assert isConnected();
        connectionHandler.send(tag, packet);
    }
    public void registerPrivilegedListener(){
        connectionHandler.addListener(TubeConnectionHandler.EVENT_RESPONSE, responseListener);
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
    public void onLoginSucceed() {
        registerPrivilegedListener();
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

package de.noucake.tubecompanion.Server;

import android.util.Log;

import de.noucake.tubecompanion.TubeCompanion;

public class TubeLoginHandler {

    private String username;
    private String password;
    private boolean credentials;
    private boolean lastLoginSucceed;
    private boolean logged;

    private final TubeCompanion main;
    private final TubeServer server;


    public TubeLoginHandler(TubeCompanion main, TubeServer server){
        this.main = main;
        this.server = server;
        credentials = false;
        lastLoginSucceed = false;
        logged = false;
    }

    public void login(String username, String password){
        if(isLoggedIn()){
            Log.d("TubeCompanion-D", "Tried to login while Already is logged in");
            return;
        }
        setLoginCredentials(username, password);
        login();
    }
    public void login(){
        if(isLoggedIn()){
            Log.d("TubeCompanion-D", "Tried to login while Already is logged in");
            return;
        }
        if(!credentials){
            //TODO
            //onLoginFailed();
            return;
        }

        if(!server.isConnected()){
            //TODO
            //onLoginFailed();
            return;
        }

        //Send Packet
        server.sendPacketDirect("login", TubePacketGenerator.generateLoginPacket(username, password));
    }

    public void onLoginResponse(int responseType){
        Log.d("TubeCompanion-D", "Login Response: "+responseType);
        String errorMessage;
        if(responseType == TubeTypes.LOGIN_SUCCESS) {
            onLoginSuccess();
        } else {
            onLoginFailed(responseType);
        }

    }
    public void onReconnect(){
        if(lastLoginSucceed){
            login();
        }
    }
    public void onDisconnect(){
        logged = false;
    }

    private void onLoginSuccess(){
        server.onLoginSucceed();
        main.onLoginSucceed(); //implicit stop login
        logged = true;
        lastLoginSucceed = true;
    }
    private void onLoginFailed(int responseType){
        switch (responseType){
            case TubeTypes.LOGIN_FAILED_ACTIV_CONNECTION:
                lastLoginSucceed = false;
                main.getHandler().displayMessage("Your are already logged in");
                main.stopLogin();
                break;
            case TubeTypes.LOGIN_FAILED_BAD_PACKET:
                lastLoginSucceed = false;
                main.getHandler().displayMessage("Received Bad Packet, Try again later.");
                main.stopLogin();
                break;
            case TubeTypes.LOGIN_FAILED_UNKNOWN_USER:
                lastLoginSucceed = false;
                main.getHandler().displayMessage("Unknown Username");
                main.requestLoginData();
                break;
            case TubeTypes.LOGIN_FAILED_WRONG_PASSWORD:
                lastLoginSucceed = false;
                main.getHandler().displayMessage("Wrong Password!");
                main.requestLoginData();
                break;
        }
    }

    public void setLoginCredentials(String username, String password){
        this.username = username;
        this.password = password;
        credentials = true;
    }
    public boolean isLoggedIn(){
        return logged && server.isConnected();
    }

}

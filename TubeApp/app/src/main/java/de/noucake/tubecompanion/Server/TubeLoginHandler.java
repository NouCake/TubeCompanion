package de.noucake.tubecompanion.Server;

import android.util.Log;

import de.noucake.tubecompanion.Server.Packets.TubePacketGenerator;
import de.noucake.tubecompanion.TubeCompanion;

public class TubeLoginHandler {

    private String username;
    private String password;
    private boolean credentials;
    private boolean lastLoginSucceed;
    private boolean logged;

    private TubeCompanion main;
    private TubeServerHandler server;


    public TubeLoginHandler(TubeCompanion main, TubeServerHandler server){
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
            onLoginFailed();
            return;
        }

        if(!server.isConnected()){
            onLoginFailed();
            return;
        }

        //Send Packet
        server.sendPacketDirect("login", TubePacketGenerator.generateLoginPacket(username, password));
    }

    public void onLoginResponse(int responseType){
        Log.d("TubeCompanion-D", "Login Response: "+responseType);
        String errorMessage;
        switch (responseType){
            case TubeTypes.LOGIN_SUCCESS:
                main.onLoginSucceed(); //implicit stop login
                logged = true;
                lastLoginSucceed = true;
                break;
            case TubeTypes.LOGIN_FAILED_ACTIV_CONNECTION:
                lastLoginSucceed = false;
                main.getHandler().displayMessage("Your are already logged in");
                main.stopLogin();
            case TubeTypes.LOGIN_FAILED_BAD_PACKET:
                lastLoginSucceed = false;
                main.getHandler().displayMessage("Received Bad Packet, Try again later.");
                main.stopLogin();
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

    public void onReconnect(){
        if(lastLoginSucceed){
            login();
        }
    }

    public void onDisconnect(){
        logged = false;
    }

    public boolean isLoggedIn(){
        return logged && server.isConnected();
    }

    private void onLoginFailed(){

    }

    public void setLoginCredentials(String username, String password){
        this.username = username;
        this.password = password;
        credentials = true;
    }



}

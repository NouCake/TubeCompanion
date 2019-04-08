package de.noucake.tubecompanion.Server.EventListener;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;

import de.noucake.tubecompanion.Server.Packets.LoginResponsePacket;
import de.noucake.tubecompanion.Server.PacketParser;
import de.noucake.tubecompanion.Server.Packets.TubePacket;
import de.noucake.tubecompanion.Server.TubeServerHandler;
import de.noucake.tubecompanion.Server.TubeTypes;

public class LoginListener implements Emitter.Listener {

    private TubeServerHandler server;

    public LoginListener(TubeServerHandler server){
        this.server = server;
    }

    @Override
    public void call(Object... args) {
        TubePacket packet = PacketParser.parse(args);
        handlePacket(packet);
    }

    private void handlePacket(TubePacket packet){
        switch(packet.getType()){
            case TubeTypes.LOGIN_RESPONSE:
                onLoginResponsePacket((LoginResponsePacket)packet);
                break;
            default:
                Log.d("TubeCompanion-D", "Given Packet is not suitable for this Listener");
        }
    }

    private void onLoginResponsePacket(LoginResponsePacket packet){
        int res = packet.getRes();
        if(packet.getRes() == 0){
            return;
        }
        server.onLoginResponse(res);
    }

}

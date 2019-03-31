package de.noucake.tubecompanion.Server.EventListener;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.Packets.LoginResponsePacket;
import de.noucake.tubecompanion.Server.Packets.PacketParser;
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
        JSONObject json = (JSONObject)args[0];
        TubePacket packet = PacketParser.parse(json);

        switch(packet.getType()){
            case TubeTypes.LOGIN_RESPONSE:
                server.onLoginResponse(((LoginResponsePacket)packet).getRes());
                break;
            default:
                Log.d("TubeCompanion-D", " NOPE");
        }
    }

}

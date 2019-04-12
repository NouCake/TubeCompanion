package de.noucake.tubecompanion.Server.EventListener;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;

import de.noucake.tubecompanion.Server.Packets.MetaDataPacket;
import de.noucake.tubecompanion.Server.TubePacketParser;
import de.noucake.tubecompanion.Server.Packets.RequestResponsePacket;
import de.noucake.tubecompanion.Server.Packets.TubePacket;
import de.noucake.tubecompanion.Server.TubeRequestHandler;
import de.noucake.tubecompanion.TubeCompanion;

public class ResponseListener implements Emitter.Listener {

    private final TubeCompanion main;
    private final TubeRequestHandler requestHandler;

    public ResponseListener(TubeCompanion main, TubeRequestHandler requestHandler){
        this.main = main;
        this.requestHandler = requestHandler;
    }

    @Override
    public void call(Object... args) {
        TubePacket packet = TubePacketParser.parse(args);
        Log.d("TubeCompanion-Packet", args[0].toString());
        if(packet != null && packet.isValid()){
            handlePacket(packet);
        }
    }

    private void handlePacket(TubePacket packet){
        Log.d("TubeCompanion-D", "Hello " + packet.getType());
        if(packet instanceof RequestResponsePacket){
            requestHandler.onResponse((RequestResponsePacket)packet);
        }
    }


}

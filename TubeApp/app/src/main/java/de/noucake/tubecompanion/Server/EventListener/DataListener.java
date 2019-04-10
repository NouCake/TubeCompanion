package de.noucake.tubecompanion.Server.EventListener;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;

import de.noucake.tubecompanion.Data.TubeData;
import de.noucake.tubecompanion.Server.PacketParser;
import de.noucake.tubecompanion.Server.Packets.PendingRequestsPacket;
import de.noucake.tubecompanion.Server.Packets.TubePacket;
import de.noucake.tubecompanion.Server.TubeTypes;
import de.noucake.tubecompanion.TubeCompanion;

public class DataListener implements Emitter.Listener {

    private final TubeCompanion main;

    public DataListener(TubeCompanion main){
        this.main = main;
    }

    @Override
    public void call(Object... args) {
        TubePacket packet = PacketParser.parse(args);
        handlePacket(packet);
    }

    private void handlePacket(TubePacket packet){
        switch(packet.getType()){
            case TubeTypes.PENDING_DOWNLOAD_REQUEST:
                onPendingRequestPacket((PendingRequestsPacket)packet);
                break;
            default:
                Log.d("TubeCompanion-D", "Given Packet is not suitable for this Listener");
        }
    }

    private void onPendingRequestPacket(PendingRequestsPacket packet){
        String ids[] = packet.getIds();
        for(int i = 0; i < ids.length; i++){
            TubeData data = new TubeData(ids[0]);
            main.addData(data);
        }
    }


}

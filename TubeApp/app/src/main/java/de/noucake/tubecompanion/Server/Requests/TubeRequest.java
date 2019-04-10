package de.noucake.tubecompanion.Server.Requests;

import android.util.Log;

import de.noucake.tubecompanion.Server.Packets.RequestResponsePacket;
import de.noucake.tubecompanion.Server.Packets.TubePacket;

public abstract class TubeRequest {

    private final int reqtype;
    private final int reqid;
    private boolean fullfilled;

    public TubeRequest(int reqid, int reqtype){
        this.reqid = reqid;
        this.reqtype = reqtype;
    }

    public void onResponse(RequestResponsePacket packet){
        if(packet.getReqID() != reqid){
            Log.d("TubeCompanion-D", "Received non matching response");
            return;
        }
        onValidResponse(packet);
    }

    protected abstract void onValidResponse(RequestResponsePacket packet);



    public boolean isFullfilled() {
        return fullfilled;
    }
}

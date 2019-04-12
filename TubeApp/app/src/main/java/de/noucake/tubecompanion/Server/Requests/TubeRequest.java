package de.noucake.tubecompanion.Server.Requests;

import android.util.Log;

import de.noucake.tubecompanion.Server.Packets.RequestResponsePacket;
import de.noucake.tubecompanion.Server.TubeTypes;

public abstract class TubeRequest {

    private final int type;
    private final int reqtype;
    private final int reqid;
    protected boolean mFullfilled;

    public TubeRequest(int reqid, int reqtype){
        this.reqid = reqid;
        this.reqtype = reqtype;

        type = TubeTypes.REQUEST;
    }

    public void onResponse(RequestResponsePacket packet){
        assert packet.isValid();
        if(packet.getReqID() != reqid){
            Log.d("TubeCompanion-D", "Received non matching response");
            return;
        }
        onValidResponse(packet);
    }
    protected abstract void onValidResponse(RequestResponsePacket packet);

    public boolean isFullfilled() {
        return mFullfilled;
    }
    public int getReqID() {
        return reqid;
    }
    public int getReqtype() {
        return reqtype;
    }
    public int getType() {
        return type;
    }
}

package de.noucake.tubecompanion.Server.Requests;

import de.noucake.tubecompanion.Server.Packets.PendingRequestsPacket;
import de.noucake.tubecompanion.Server.Packets.RequestResponsePacket;
import de.noucake.tubecompanion.Server.Packets.TubePacket;
import de.noucake.tubecompanion.Server.TubeTypes;

public class PendingRequestsRequest extends TubeRequest {

    private String[] ids;

    public PendingRequestsRequest(int reqid){
        super(reqid, TubeTypes.REQUEST_PENDING);
    }

    @Override
    protected void onValidResponse(RequestResponsePacket p) {
        assert p instanceof PendingRequestsPacket;

        PendingRequestsPacket packet = (PendingRequestsPacket)p;
        ids = packet.getIds();

        mFullfilled = true;
    }

    public String[] getIds() {
        return ids;
    }
}

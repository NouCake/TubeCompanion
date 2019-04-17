package de.noucake.tubecompanion.Server.Requests;

import android.util.Log;

import de.noucake.tubecompanion.Server.Packets.FilePacket;
import de.noucake.tubecompanion.Server.Packets.RequestResponsePacket;
import de.noucake.tubecompanion.Server.TubeTypes;

public class FileRequest extends TubeRequest {

    private String id;
    private int filetype;
    private int chunksize;
    private int startbyte;
    private int currentPacket;
    private int totalPackets;
    private byte[] data;

    public FileRequest(int reqid, String id, int filetype) {
        super(reqid, TubeTypes.REQUEST_FILE);
        this.filetype = filetype;
        this.id = id;
    }

    @Override
    protected void onValidResponse(RequestResponsePacket p) {
        assert p instanceof FilePacket;
        FilePacket packet = (FilePacket)p;

        if(filetype != packet.getFiletype() || !id.matches(packet.getId())){
            return;
        }

        Log.d("TubeCompanion-D", "CurrentPacket: " + currentPacket);
    }

    public String getId() {
        return id;
    }

    public int getFiletype() {
        return filetype;
    }
}

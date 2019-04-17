package de.noucake.tubecompanion.Server.Requests;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.noucake.tubecompanion.Server.Packets.FilePacket;
import de.noucake.tubecompanion.Server.Packets.RequestResponsePacket;
import de.noucake.tubecompanion.Server.TubeTypes;

public class FileRequest extends TubeRequest {

    private String id;
    private int filetype;
    private int filesize;
    private int currentPacket;
    private int totalPackets;

    private ByteArrayOutputStream buffer;

    public FileRequest(int reqid, String id, int filetype) {
        super(reqid, TubeTypes.REQUEST_FILE);
        this.filetype = filetype;
        this.id = id;
    }

    @Override
    protected void onValidResponse(RequestResponsePacket p) {
        assert p instanceof FilePacket;
        assert !mFullfilled;
        FilePacket packet = (FilePacket)p;

        if(filetype != packet.getFiletype() || !id.matches(packet.getId())){
            return;
        }

        if(((FilePacket) p).getCurrentPacket() == 0){
            onFirstPacket(packet);
        }

        if(currentPacket != packet.getCurrentPacket()){
            Log.d("TubeCompanion-D", "Non matching Packet Number");
            Log.d("TubeCompanion-D", "CurrentPacket: " + currentPacket + " Packet.CurrentPacket: " + packet.getCurrentPacket());
            return;
        }

        try {
            buffer.write(packet.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentPacket++;

        if(currentPacket == totalPackets){
            mFullfilled = true;
        }

    }

    private void onFirstPacket(FilePacket packet){
        filesize = packet.getFileSize();
        totalPackets = packet.getTotalPackets();

        buffer = new ByteArrayOutputStream(filesize);
    }

    public String getId() {
        return id;
    }

    public int getFiletype() {
        return filetype;
    }

    public byte[] getData(){
        return buffer.toByteArray();
    }

    public int getFilesize() {
        return filesize;
    }
}

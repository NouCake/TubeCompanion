package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONException;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.TubeTypes;

public class FilePacket extends RequestResponsePacket {

    private String id;
    private int filetype;
    private int filesize;
    private int chunksize;
    private int byteoffset;
    private int currentPacket;
    private int totalPackets;
    private byte[] data;

    public FilePacket(JSONObject raw) {
        super(TubeTypes.FILE, raw);
    }

    @Override
    protected void parseData() throws JSONException {
        super.parseData();

        this.id = mRaw.getString("id");
        this.filetype = mRaw.getInt("filetype");
        this.filesize = mRaw.getInt("filesize");
        this.chunksize = mRaw.getInt("chunksize");
        this.byteoffset = mRaw.getInt("byteoffset");
        this.currentPacket = mRaw.getInt("currentPacket");
        this.totalPackets = mRaw.getInt("totalPackets");
        this.data = (byte[])mRaw.get("data");

    }

    public int getFiletype() {
        return filetype;
    }
    public String getId() {
        return id;
    }
    public int getChunksize() {
        return chunksize;
    }
    public int getByteoffset() {
        return byteoffset;
    }
    public byte[] getData() {
        return data;
    }
    public int getCurrentPacket() {
        return currentPacket;
    }
    public int getTotalPackets() {
        return totalPackets;
    }
    public int getFileSize() {
        return filesize;
    }

}

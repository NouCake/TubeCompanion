package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONObject;

public abstract class TubePacket {

    private final int type;

    protected final JSONObject mRaw;
    protected boolean mValid;

    public TubePacket(int type, JSONObject packet){
        this.type = type;
        this.mRaw = packet;
        mValid = false;
    }

    abstract void parse();

    public int getType() {
        return type;
    }

    public boolean isValid() {
        return mValid;
    }
}
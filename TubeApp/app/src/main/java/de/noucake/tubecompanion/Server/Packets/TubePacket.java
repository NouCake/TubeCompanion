package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONException;
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

    public void parse(){
        try {
            parseData();
        } catch (JSONException e) {
            mValid = false;
        }
    }

    protected abstract void parseData() throws JSONException;

    public int getType() {
        return type;
    }

    public boolean isValid() {
        return mValid;
    }
}
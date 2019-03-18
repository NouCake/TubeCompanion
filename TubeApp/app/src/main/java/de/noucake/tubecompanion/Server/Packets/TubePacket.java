package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONObject;

public abstract class TubePacket {

    private JSONObject raw;
    private boolean valid;
    private String error;
    private final int type;

    public TubePacket(JSONObject packet, int type){
        this.raw = packet;
        this.type = type;

        valid = extractData();
    }

    protected abstract boolean extractData();

    protected void setError(String error) {
        valid = false;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public boolean isValid() {
        return valid;
    }

    public JSONObject getRawPacket() {
        return raw;
    }

    public int getType() {
        return type;
    }


}
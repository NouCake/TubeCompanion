package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RequestResponsePacket extends TubePacket {

    private int reqID;

    public RequestResponsePacket(int type, JSONObject raw){
        super(type, raw);
    }

    protected void parseData() throws JSONException{
        reqID = mRaw.getInt("reqid");
    }

    public int getReqID() {
        return reqID;
    }
}

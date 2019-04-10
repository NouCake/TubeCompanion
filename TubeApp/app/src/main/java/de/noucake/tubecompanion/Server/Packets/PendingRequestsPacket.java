package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.TubeTypes;

public class PendingRequestsPacket extends RequestResponsePacket {

    private String[] ids;

    public PendingRequestsPacket(JSONObject packet) {
        super(TubeTypes.PENDING_DOWNLOAD_REQUEST, packet);
    }

    @Override
    public void parseData() throws JSONException{
        super.parseData();

        JSONArray oId = mRaw.getJSONArray("ids");
        ids = new String[oId.length()];
        for(int i = 0; i < ids.length; i++){
            ids[i] = oId.getString(i);
        }
    }

    public String[] getIds() {
        return ids;
    }
}

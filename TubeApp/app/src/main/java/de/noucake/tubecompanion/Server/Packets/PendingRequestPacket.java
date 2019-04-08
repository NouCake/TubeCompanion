package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.TubeTypes;

public class PendingRequestPacket extends TubePacket {

    private String[] ids;

    public PendingRequestPacket(JSONObject packet) {
        super(TubeTypes.PENDING_DOWNLOAD_REQUEST, packet);
    }

    @Override
    void parse() {
        try{
            JSONArray oId = mRaw.getJSONArray("ids");
            ids = new String[oId.length()];
            for(int i = 0; i < ids.length; i++){
                ids[i] = oId.getString(i);
            }
        } catch (JSONException e){
            mValid = false;
            return;
        }
    }

    public String[] getIds() {
        return ids;
    }
}

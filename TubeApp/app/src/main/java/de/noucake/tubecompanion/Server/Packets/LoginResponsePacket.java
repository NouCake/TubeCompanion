package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONException;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.TubeTypes;

public class LoginResponsePacket extends TubePacket {

    private int res;

    public LoginResponsePacket(JSONObject packet) {
        super(packet, TubeTypes.LOGIN_RESPONSE);
    }

    @Override
    protected boolean extractData() {
        int type;
        try {
            type = getRawPacket().getInt("type");
            res = getRawPacket().getInt("res");
        } catch (JSONException e) {
            setError("JSON Error: couldn't gather attributes");
            return false;
        }
        if(type != this.getType()){
            setError("Type is Wrong!");
            return false;
        }
        return true;
    }

    public int getRes() {
        return res;
    }
}

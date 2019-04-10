package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONException;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.TubeTypes;

public class LoginResponsePacket extends TubePacket {

    private int res;

    public LoginResponsePacket(JSONObject packet) {
        super(TubeTypes.LOGIN_RESPONSE, packet);
    }

    @Override
    protected void parseData() throws JSONException{
        res = mRaw.getInt("res");
    }

    public int getRes() {
        return res;
    }
}

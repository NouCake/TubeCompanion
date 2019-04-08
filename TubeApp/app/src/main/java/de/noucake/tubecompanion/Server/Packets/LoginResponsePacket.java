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
    void parse() {
        try {
            res = mRaw.getInt("res");
            mValid = true;
        } catch (JSONException e) {
            mValid = false;
            return;
        }
    }

    public int getRes() {
        return res;
    }
}

package de.noucake.tubecompanion.Server.Packets;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.TubeTypes;

public class PacketParser {


    public static TubePacket parse(JSONObject obj){

        int type = -1;
        try {
            type = obj.getInt("type");
        } catch (JSONException e) {
            Log.d("TubeCompanion-D", "Received weird packet, this should not happen \n"+obj.toString());
            return null;
        }

        switch(type){
            case TubeTypes.LOGIN_RESPONSE:
                return new LoginResponsePacket(obj);
            default:
                Log.d("TubeCompanion-D", "Received packet with unknown type :" + type);
                break;
        }

        return null;
    }



}

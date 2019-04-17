package de.noucake.tubecompanion.Server;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.Packets.FilePacket;
import de.noucake.tubecompanion.Server.Packets.LoginResponsePacket;
import de.noucake.tubecompanion.Server.Packets.MetaDataPacket;
import de.noucake.tubecompanion.Server.Packets.PendingRequestsPacket;
import de.noucake.tubecompanion.Server.Packets.TubePacket;

public class TubePacketParser {

    private TubePacketParser(){
    }

    /**
     * Gets the input of a Emitter.Listener and returns a TubePacket
     * Returns null if given args are null, not parseable or type of packet is unknown or not valid
     * @param args input of Emitter.Listener
     * @return TubePacket or Null
     */
    public static TubePacket parse(Object... args){
        if(args.length <= 0 || !(args[0] instanceof JSONObject)){
            Log.d("TubeCompanion-D", "Parser called with Invalid args");
            return null;
        }

        JSONObject o = (JSONObject)args[0];
        int type = getType(o);
        TubePacket packet =  getPacket(type, o);
        packet.parse();
        return packet;
    }

    private static TubePacket getPacket(int type, JSONObject o){
        switch(type){
            case TubeTypes.LOGIN_RESPONSE:
                return new LoginResponsePacket(o);
            case TubeTypes.PENDING_DOWNLOAD_REQUEST:
                return new PendingRequestsPacket(o);
            case TubeTypes.META_DATA:
                return new MetaDataPacket(o);
            case TubeTypes.FILE:
                return new FilePacket(o);
        }
        Log.d("TubeCompanion-D","Unsupported Packet Type " + type);
        return null;
    }

    private static int getType(JSONObject json){
        try {
            return json.getInt("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }



}

package de.noucake.tubecompanion.Server;

import org.json.JSONException;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.Packets.LoginResponsePacket;
import de.noucake.tubecompanion.Server.Packets.PendingRequestPacket;
import de.noucake.tubecompanion.Server.Packets.TubePacket;

public class PacketParser {


    /**
     * Gets the input of a Emitter.Listener and returns a TubePacket
     * Returns null if given args are null, not parseable or type of packet is unknown or not valid
     * @param args input of Emitter.Listener
     * @return TubePacket or Null
     */
    public static TubePacket parse(Object... args){
        if(args.length <= 0 || !(args[0] instanceof JSONObject)){
            return null;
        }

        JSONObject o = (JSONObject)args[0];
        int type = getType(o);
        TubePacket packet =  getPacket(type, o);
        if(packet.isValid()){
            return packet;
        }

        return null;
    }

    private static TubePacket getPacket(int type, JSONObject o){
        switch(type){
            case TubeTypes.LOGIN_RESPONSE:
                return new LoginResponsePacket(o);
            case TubeTypes.PENDING_DOWNLOAD_REQUEST:
                return new PendingRequestPacket(o);
        }
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

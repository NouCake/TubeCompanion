package de.noucake.tubecompanion.Server;

import de.noucake.tubecompanion.Server.TubeTypes;

public class TubePacketGenerator {

    private TubePacketGenerator(){

    }

    public static String generateLoginPacket(String username, String password){

        String packet = "{";
        packet += generateAttribute("type", TubeTypes.LOGIN, true);
        packet += generateAttribute("username", username, true);
        packet += generateAttribute("password", password,true);
        packet += generateAttribute("apptype", TubeTypes.LOGIN_DEVICE,false);
        packet += "}";
        return packet;
    }

    public static String generateRequestPendingPacket(int reqid, int reqtype){
        String packet = "{";
        packet += generateAttribute("type", 0, true);
        packet += generateAttribute("reqid", reqid, true);
        packet += generateAttribute("reqtype", reqtype, false);
        return packet;
    }

    /**
    public static String generateRequestPacket(TubeRequest req){
        String packet = "{";
        packet += generateAttribute("type", TubeTypes.REQUEST,true);
        packet += generateAttribute("reqid", req.getReqId(), true);
        packet += generateAttribute("reqtype", req.getRequestedType(),false);
        switch(req.getRequestedType()){
            case ServerTypes.FILE:
                FileRequest fr = (FileRequest)req;
                packet += ",";
                packet += generateAttribute("filetype", fr.getFiletype(),false);
            case ServerTypes.META_DATA:
                packet += ",";
                packet += generateAttribute("id", ((DataRequest)req).getId(),false);
                break;
            default:
                break;
        }
        packet += "}";
        return packet;
    }
    */

    private static String generateAttribute(String attribute, String value, boolean comma){
        return "\""+attribute+"\":\""+value+"\"" + (comma ? ",":"");
    }

    private static String generateAttribute(String attribute, int value, boolean comma){
        return "\""+attribute+"\":"+value + (comma ? ",":"");
    }

}

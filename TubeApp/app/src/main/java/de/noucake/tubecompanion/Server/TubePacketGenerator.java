package de.noucake.tubecompanion.Server;

import de.noucake.tubecompanion.Server.Requests.FileRequest;
import de.noucake.tubecompanion.Server.Requests.MetaDataRequest;
import de.noucake.tubecompanion.Server.Requests.TubeRequest;
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
    public static String generateMetaDataRequest(MetaDataRequest req){
        return generateMetaDataRequest(req.getType(), req.getReqID(), req.getReqtype(), req.getId());
    }
    public static String generateRequestPacket(TubeRequest req){

        switch (req.getReqtype()){
            case TubeTypes.REQUEST_PENDING:
                return generatePendingRequestsRequest(req.getType(), req.getReqID(), req.getReqtype());
            case TubeTypes.REQUEST_META:
                return generateMetaDataRequest((MetaDataRequest) req);
            case TubeTypes.REQUEST_FILE:
                return  generateFileRequest((FileRequest) req);
        }

        return "";
    }
    public static String generateFileRequest(FileRequest req){
        return generateFileRequest(req.getType(), req.getReqID(), req.getReqtype(), req.getId(), req.getFiletype());
    }

    private static String generatePendingRequestsRequest(int type, int reqid, int reqtype){
        String packet = "{";
        packet += generateAttribute("type", type, true);
        packet += generateAttribute("reqid", reqid, true);
        packet += generateAttribute("reqtype", reqtype, false);
        packet += "}";
        return packet;
    }
    private static String generateMetaDataRequest(int type, int reqid, int reqtype, String id){
        String packet = "{";
        packet += generateAttribute("type", type, true);
        packet += generateAttribute("reqid", reqid, true);
        packet += generateAttribute("reqtype", reqtype, true);
        packet += generateAttribute("id", id, false);
        packet += "}";
        return packet;
    }
    private static String generateFileRequest(int type, int reqid, int reqtype, String id, int filetype){
        String packet = "{";
        packet += generateAttribute("type", type, true);
        packet += generateAttribute("reqid", reqid, true);
        packet += generateAttribute("reqtype", reqtype, true);
        packet += generateAttribute("id", id, true);
        packet += generateAttribute("filetype", filetype, false);
        packet += "}";
        return packet;
    }

    private static String generateAttribute(String attribute, String value, boolean comma){
        return "\""+attribute+"\":\""+value+"\"" + (comma ? ",":"");
    }
    private static String generateAttribute(String attribute, int value, boolean comma){
        return "\""+attribute+"\":"+value + (comma ? ",":"");
    }

}

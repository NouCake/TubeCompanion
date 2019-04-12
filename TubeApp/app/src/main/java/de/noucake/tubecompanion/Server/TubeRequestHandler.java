package de.noucake.tubecompanion.Server;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import de.noucake.tubecompanion.Data.TubeData;
import de.noucake.tubecompanion.Server.Packets.RequestResponsePacket;
import de.noucake.tubecompanion.Server.Requests.MetaDataRequest;
import de.noucake.tubecompanion.Server.Requests.PendingRequestsRequest;
import de.noucake.tubecompanion.Server.Requests.TubeRequest;
import de.noucake.tubecompanion.TubeCompanion;

public class TubeRequestHandler {

    private static int nextReqID;
    public static int getNextReqID(){
        return nextReqID++;
    }

    private final TubeCompanion main;
    private List<TubeRequest> requests;

    public TubeRequestHandler(TubeCompanion main){
        this.main = main;
        requests = new LinkedList<>();
    }

    private void onRequestFullfilled(TubeRequest req){
        switch (req.getReqtype()){
            case TubeTypes.REQUEST_PENDING:
                onPendingRequestsRequestFullfilled((PendingRequestsRequest)req);
                break;
            case TubeTypes.REQUEST_META:
                onMetaDataRequestFullfilled((MetaDataRequest)req);
                break;
        }
    }
    private void onPendingRequestsRequestFullfilled(PendingRequestsRequest req){
        for(String id : req.getIds()){
            main.addData(new TubeData(id));
        }
    }
    private void onMetaDataRequestFullfilled(MetaDataRequest req){
        TubeData data = main.getDataByID(req.getId());
        if(data == null){
            Log.d("TubeCompanion-D", "Error, No TubeData for requested data");
            return;
        }

        data.setTitle(req.getTitle());
        //data.setImagesize(req.getImagesize());
        //data.setAudiosize(req.getAudiosize());

        main.onTubeDataUpdated();
    }

    public void onResponse(RequestResponsePacket packet){
        Log.d("TubeCoompanion-D", "OnResponse : " + requests.size());
        TubeRequest req = getRequestByReqID(packet.getReqID());
        if(req == null){
            Log.d("TubeCoompanion-D", "Could not find Request with this ReqID");
            return;
        }

        req.onResponse(packet);
        if(req.isFullfilled()){
            onRequestFullfilled(req);
            Log.d("TubeCompanion-D", "Request Removed");
            requests.remove(req);
        }
    }
    public void addRequest(TubeRequest req){
        Log.d("TubeCompanion-D", "Request Added");
        requests.add(req);
    }

    private TubeRequest getRequestByReqID(int reqid){
        for(TubeRequest req : requests){
            if(req.getReqID() == reqid){
                return req;
            }
        }
        return null;
    }

}

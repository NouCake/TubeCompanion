package de.noucake.tubecompanion.Server.Requests;

import android.util.Log;

import de.noucake.tubecompanion.Server.Packets.MetaDataPacket;
import de.noucake.tubecompanion.Server.Packets.RequestResponsePacket;
import de.noucake.tubecompanion.Server.TubeTypes;

public class MetaDataRequest extends TubeRequest {

    private String id;
    private String title;
    private String author;
    //private int imagesize;
    //private int audiosize;
    //private int videosize;

    public MetaDataRequest(int reqid, String id) {
        super(reqid, TubeTypes.REQUEST_META);
        this.id = id;
    }

    @Override
    protected void onValidResponse(RequestResponsePacket p) {
        assert p instanceof MetaDataPacket;
        MetaDataPacket packet = (MetaDataPacket)p;

        if(packet.getId().matches(id)){
            title = packet.getTitle();
            //imagesize = packet.getImagesize();
            //audiosize = packet.getAudiosize();
            mFullfilled = true;
        } else {
            Log.d("TubeCompanion-D", "No matching ID");
        }
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
    /*
    public int getImagesize() {
        return imagesize;
    }
    public int getAudiosize() {
        return audiosize;
    }
    */

}

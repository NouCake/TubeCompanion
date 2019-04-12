package de.noucake.tubecompanion.Server.Packets;

import org.json.JSONException;
import org.json.JSONObject;

import de.noucake.tubecompanion.Server.TubeTypes;

public class MetaDataPacket extends RequestResponsePacket {

    private String id;
    private String title;
    //private int imagesize;
    //private int audiosize;

    public MetaDataPacket(JSONObject packet) {
        super(TubeTypes.META_DATA, packet);
    }

    @Override
    protected void parseData() throws JSONException {
        super.parseData();

        id = mRaw.getString("id");
        title = mRaw.getString("title");
        //imagesize = mRaw.getInt("imagesize");
        //audiosize = mRaw.getInt("audiosize");
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

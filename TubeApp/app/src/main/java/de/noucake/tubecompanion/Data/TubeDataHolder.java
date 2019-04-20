package de.noucake.tubecompanion.Data;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class TubeDataHolder {

    private List<TubeData> data;

    public TubeDataHolder(){
        data = new LinkedList<>();
    }

    public boolean addData(TubeData data){
        if(this.data.contains(data) || findByID(data.getId()) != null){
            Log.d("TubeCompanion-D", "Tried to add Multiple TubeData with same ID");
            return false;
        }
        this.data.add(data);
        return true;
    }

    public boolean removeData(TubeData data){
        return this.data.remove(data);
    }

    public TubeData findByID(String ID){
        for(TubeData d : data){
            if(ID.matches(d.getId()))
                return d;
        }
        return null;
    }

    public List<TubeData> getData() {
        return data;
    }
}

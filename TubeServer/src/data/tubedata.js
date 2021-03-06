class TubeData {
    constructor(id){
        this.id = id;

        this.title = false;
        this.audiosize = -1;
        this.imagesize = -1;
        this.videosize = -1;

        this._complete = false;
        this._hasMeta = false;
        this._hasImage = false;
        this._hasAudio = false;
        this._hasVideo = false;

        //for Downloader
        this.downloading = false;
    }

    /**
     * renames attributes and cherrypicks relevant data for saving
     */
    getSaveData(){
        return {
            id:this.id,
            title:this.title,
            imagesize:this.imagesize,
            audiosize:this.audiosize,
            videosize:this.videosize,
            meta:this._hasMeta,
            image:this._hasImage,
            audio:this._hasAudio,
            video:this._hasVideo,
            complete:this._complete
        }
    }

    updateCompleteStatus(){
        if(this._hasMeta && this._hasImage && this._hasAudio && this._hasVideo){
            this._complete = true;
        }
        return this._complete;
    }
    
    setImage(size){
        this._hasImage = true;
        this.imagesize = size;
        this.updateCompleteStatus();
    }

    setAudio(size){
        this._hasAudio = true;
        this.audiosize = size;
        this.updateCompleteStatus();
    }

    setVideo(size){
        this._hasVideo = true;
        this.videosize = size;
        this.downloading = false;
        this.updateCompleteStatus();
    }

    setMeta(title){
        this._hasMeta = true;
        this.title = title;
        this.updateCompleteStatus();
    }

    isComplete(){
        return this._complete;
    }
}

TubeData.create = function(ref){
    if(ref.id == null){
        console.log("Error while creating DataObject:\nCalled with bad reference", ref);
        return;
    }

    //checks if read data has complete attribute set
    //indicates if working on an old fileset
    let data = new TubeData(ref.id);
    for(let attr in data.getSaveData()){
        if(ref[attr] == null){
            console.log("Error while creating DataObject:\nreference is missing:" + attr, ref);
            return;
        }
        data[attr] = ref[attr];
    }
    TubeData.rename(data);
    return data;
    
}

TubeData.rename = function(data){
    //Renaming attributes
    data._complete = data.complete;
    data._hasMeta = data.meta;
    data._hasImage = data.image;
    data._hasAudio = data.audio;
    data._hasVideo = data.video;
    data.meta = null;
    data.image = null;
    data.audio = null;
    data.video = null;
}

module.exports = TubeData;
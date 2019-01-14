class TubeData {
    constructor(id){
        this.id = id;

        this.title = false;
        this.audiosize = -1;
        this.imagesize = -1;

        this._complete = false;
        this._hasImage = false;
        this._hasAudio = false;
    }

    getSaveData(){
        return {
            id:this.id,
            title:this.title,
            audiosize:this.audiosize,
            imagesize:this.imagesize,
            audio:this._hasAudio,
            image:this._hasImage,
            complete:this._complete
        }
    }

    updateCompleteStatus(){
        if(this.title && this._hasImage && this._hasAudio){
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

    setTitle(title){
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

    //Renaming attributes;
    ref._complete = ref.complete;
    ref._hasAudio = ref.audio;
    ref._hasImage = ref.image;

    let data = new TubeData(ref.id);
    for(let attr in data){
        if(ref[attr] == null){
            console.log("Error while creating DataObject:\nrefference is missing " + attr, ref);
            return;
        }
        data[attr] = ref[attr];
    }
    return data;
}

module.exports = TubeData;
const fs = require('fs');
const TubeData = require('./tubedata');
const TubeTypes = require('../tubetypes')

class DataHandler{

    constructor(main){
        this.main = main;
    
        this.data = {};
        this.dataPath = './data/';
        this.indexSavePath = './data/index';

        this.createDataFolder();
    }

    createDataFolder(){
        if(!fs.existsSync(this.dataPath)){
            fs.mkdirSync(this.dataPath);
        } else if(!fs.existsSync(this.indexSavePath)){
            fs.appendFileSync(this.indexSavePath, '{}', err => {
                console.log("Error while creating data folder\n", err);
            });
        }
    }

    addData(id){
        if(this.data[id] != null){
            console.log("Entry with this id already exists");
            return;
        }
        this.data[id] = new TubeData(id);
    }

    hasData(id){
        return this.data[id] != null;
    }

    /**
     * returns the requested data, if data does not exist returns null/undefined
     * @param {String} id 
     * @returns {TubeData}
     */
    getData(id){
        return this.data[id];
    }

    /**
     * returns path to desired file
     * @param {TubeData} data 
     * @param {FILE_TYPE} FILE_TYPE TubeTypes.FileTypes
     */
    getDataFilePath(data, FILE_TYPE){
        let path = this.dataPath + data.id + "/" + data.id;
        let ending = ".error";
        switch(FILE_TYPE){
            case TubeTypes.FILE_IMAGE:
                ending = ".jpg";
            break;
            case TubeTypes.FILE_AUDIO:
            ending = ".m4a";
            break;
            case TubeTypes.FILE_VIDEO:
            ending = ".mp4";
            break;
        }
        return path + ending
    }

    addTitle(id, title){
        if(!this.data[id] == null){
            console.log("No Entry with this id found");
            return;
        }
        this.data[id].setTitle(title);
    }

    addAudio(id, size){
        if(!this.data[id] == null){
            console.log("No Entry with this id found");
            return;
        }
        this.data[id].setAudio(size);
    }

    addImage(id, size){
        if(!this.hasData(id)){
            console.log("No Entry with this id found");
            return;
        }
        this.data[id].setImage(size);
    }

    saveAllData(){
        let toSave = {};
        for(let d in this.data){
            toSave[d] = this.data[d].getSaveData();
        }
        fs.writeFileSync(this.indexSavePath, JSON.stringify(toSave), (err) => {
            console.log("An error accoured while saving:\n",err);
        });
    }

    loadAllData(){
        let index = fs.readFileSync(this.indexSavePath, 'utf8');
        try{
            index = JSON.parse(index);
        } catch(e){
            console.log("Error: Index file has been corrupted\n",e);
        }
        for(let i in index){
            let savedat = TubeData.create(index[i]);
            if(savedat){
                this.data[i] = savedat
                if(!this.data[i].isComplete()){
                    this.onIncompleteDataLoaded(this.data[i]);
                }
            }
        }
        console.log("Loaded",this.countData(), "TubeData Objects");
    }

    countData(){
        let counter = 0;
        for(let id in this.data){
            counter++;
        }
        return counter;
    }

    onIncompleteDataLoaded(data){
        console.log("Data with id",data.id,"is incomplete");
    }


}

module.exports = DataHandler;
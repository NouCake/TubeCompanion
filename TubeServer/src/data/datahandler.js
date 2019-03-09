const fs = require('fs');
const TubeData = require('./tubedata');
const TubeTypes = require('../tubetypes')
const DataLoader = require('./dataloader');

class DataHandler{

    constructor(main){
        this.main = main;
    
        this.dataPath = './data/';
        this.indexSavePath = './data/index';
        this.loader = new DataLoader();
        this.data = {};

        this.createDataFolder();
    }

    createDataFolder(){
        this.loader.createFolder(this.dataPath);
        this.loader.createFile(this.indexSavePath, "{}");
    }

    /**
     * Creates Folder for given data in DataPath
     * @param {TubeData} data 
     */
    createTubeDataFolder(data){
        this.loader.createFolder(this.dataPath + data.id);
    }

    getTubeDataFolder(data){
        return this.dataPath + data.id + "/";
    }

    /**
     * @returns {Array<TubeData>} 
     */
    getIncompleteData(){
        let incomplete = [];
        for(let data in this.data){
            if(!this.data[data].isComplete){
                incomplete.push(data);
            }
        }
        return incomplete.length ? incomplete : null;
    }

    /**
     * creates new TubeData object and adds to list
     * @param {String} id 
     * @returns {TubeData}
     */
    addData(id){
        if(this.data[id] != null){
            console.log("Entry with this id already exists");
            return this.data[id];
        }
        this.data[id] = new TubeData(id);
        return this.data[id];
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
    
    /**
     * 
     * @param {TubeData} data 
     * @param {Number} FILE_TYPE 
     */
    getDataFileSize(data, FILE_TYPE){
        switch(FILE_TYPE){
            case TubeTypes.FILE_IMAGE:
                return data.imagesize;

            case TubeTypes.FILE_AUDIO:
                return data.audiosize;

            case TubeTypes.FILE_VIDEO:
                return data.videosize;
        }
    }

    getDataFile(data, FILE_TYPE, chunksize, callback){
        this.loader.readFileAsync(this.getDataFilePath(data, FILE_TYPE), chunksize, callback);
    }

    /**
     * @param {TubeData | String} data 
     * @returns {TubeData}
     */
    _isDataAndGet(data){
        if(!(data instanceof TubeData)){
            data = this.data[id];
            if(!data){
                console.log("No Entry with this id found");
                return;
            }
        }
        return data;
    }

    /**
     * @param {TubeData | String} id 
     * @param {String} title 
     */
    addTitle(data, title){
        data = this._isDataAndGet(data);
        if(!data) return;

        data.setMeta(title);
        if(data.isComplete()){
            this.onDataComplete(data);
        }
    }

    /**
     * @param {TubeData | String} id 
     * @param {String} title 
     */
    addAudio(data, size){
        data = this._isDataAndGet(data);
        if(!data) return;
        
        data.setAudio(size);
        if(data.isComplete()){
            this.onDataComplete(data);
        }
    }

    /**
     * @param {TubeData | String} id 
     * @param {String} title 
     */
    addImage(data, size){
        data = this._isDataAndGet(data);
        if(!data) return;

        data.setImage(size);
        if(data.isComplete()){
            this.onDataComplete(data);
        }
    }

    /**
     * @param {TubeData | String} id 
     * @param {String} title 
     */
    addVideo(data, size){
        data = this._isDataAndGet(data);
        if(!data) return;

        data.setVideo(size);
        if(data.isComplete()){
            this.onDataComplete(data);
        }
    }

    /**
     * 
     * @param {TubeData} data 
     */
    onDataComplete(data){
        console.log("data is complete", data.id);
        this.saveAllData();
    }

    saveAllData(){
        let toSave = {};
        for(let d in this.data){
            toSave[d] = this.data[d].getSaveData();
        }
        this.loader.writeFile(this.indexSavePath, JSON.stringify(toSave));
    }

    loadAllData(){
        let index = this.loader.readFile(this.indexSavePath, true);
        if(!index) return;

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
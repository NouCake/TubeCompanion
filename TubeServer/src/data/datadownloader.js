//For Docs
const TubeData = require('./tubedata');
const DataHandler = require('./datahandler');

const TubeTypes = require('../tubetypes')
const ytdl = require('ytdl-core');
const fs = require('fs');

class DataDownloader{

    /**
     * @param {DataHandler} datman 
     */
    constructor(datman){
        this.datman = datman;
    }

    /**
     * downloads a give youtube video / audio / thumbnail
     * 
     * checks for an entry in datahandler
     *      when: checks if entry is complete
     * otherwise creates a new entry
     * then starts downloading (missing) files.
     * @param {String} id Youtube Video ID
     */
    requestDownload(id){
        if(!ytdl.validateID(id)){
            console.log("id is not valid", id);
            return;
        }

        /** @type {TubeData} */
        let data; 
        if(this.datman.hasData(id)){
            data = this.datman.getData(id);
        } else {
            data = this.datman.addData(id);
        }

        if(data.isComplete()){
            console.log("Data is already complete");
            return;
        }

        if(!data._hasMeta){
            downloadMetaInformation(data);
        }
        if(!data._hasImage){
            downloadThumbnail(data);
        }
        if(!data._hasAudio){
            downloadAudio(data);
        }
        if(!data._hasVideo){
            downloadVideo(data);
        }
        
    }

    /**
     * Uses ytdl to get various meta information
     * currently meta informations contain:
     * Title
     * @param {TubeData} data 
     */
    downloadMetaInformation(data){
        //TODO
        //data.setMeta(title);
    }


    /**
     * Uses ytdl to get thumbnail
     * @param {TubeData} data 
     */
    downloadThumbnail(data){
        //TODO
        //fs.appendFileSync(this.datman.getDataFilePath(data, TubeTypes.FILE_IMAGE)); SYNC OR NOT SYNC!
        //data.setImage(size);
    }

    /**
     * Uses ytdl to get audio file
     * @param {TubeData} data 
     */
    downloadAudio(data){
        //TODO
        //fs.appendFileSync(this.datman.getDataFilePath(data, TubeTypes.FILE_AUDIO)); SYNC OR NOT SYNC!
        //data.setAudio(size);
    }

    /**
     * Uses ytdl to get video file
     * @param {TubeData} data 
     */
    downloadVideo(data){
        //TODO
        //fs.appendFileSync(this.datman.getDataFilePath(data, TubeTypes.FILE_VIDEO)); SYNC OR NOT SYNC!
        //data.setVideo(size);
    }

    

}
//For Docs
const TubeData = require('./tubedata');
const DataHandler = require('./datahandler');
const t = require('../../lib/videoinfo');

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
     * Initializes a download for every incomplete dataset given by DataHandler
     */
    downloadIncompleteData(){
        let data = this.datman.getIncompleteData();
        if(data){
            for(let i in this.data){
                requestDownload(data[i].id);
            }
        }
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

        

        
        
    }

    getYTDLandDownload(data){
        ytdl.getInfo(data.id).then(info => {
            //check if info is valid?
            //what if bad id is handed

        

            if(!data._hasMeta){
                downloadMetaInformation(data, info);
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
            
        });
    }

    /**
     * Uses ytdl to get various meta information
     * currently meta informations contain:
     * Title
     * @param {TubeData} data 
     */
    downloadMetaInformation(data, ytdl_info){
        //TODO
        data.setMeta(ytdl_info.title);
    }


    /**
     * Uses ytdl to get thumbnail
     * @param {TubeData} data 
     */
    downloadThumbnail(data, ytdl_info){
        //TODO
        //fs.appendFileSync(this.datman.getDataFilePath(data, TubeTypes.FILE_IMAGE)); SYNC OR NOT SYNC!
        //data.setImage(size);
    }

    /**
     * Uses ytdl to get audio file
     * @param {TubeData} data 
     */
    downloadAudio(data, ytdl_info){
        //TODO
        //fs.appendFileSync(this.datman.getDataFilePath(data, TubeTypes.FILE_AUDIO)); SYNC OR NOT SYNC!
        //data.setAudio(size);
    }

    /**
     * Uses ytdl to get video file
     * @param {TubeData} data
     */
    downloadVideo(data, ytdl_info){
        //TODO
        //fs.appendFileSync(this.datman.getDataFilePath(data, TubeTypes.FILE_VIDEO)); SYNC OR NOT SYNC!
        //data.setVideo(size);
    }

    

}

module.exports = DataDownloader;
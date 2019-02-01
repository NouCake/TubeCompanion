//For Docs
const DataHandler = require('./datahandler');

const ytdl = require('ytdl-core');

class DataDownloader{

    /**
     * @param {DataHandler} datman 
     */
    constructor(datman){
        this.datman = datman;
    }

    /**
     * downloads a give youtube video / audio / thumbnail
     * @param {String} id Youtube Video ID
     */
    download(id){
        
    }

}
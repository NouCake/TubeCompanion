//For Docs
const TubeData = require('./tubedata');
const DataHandler = require('./datahandler');

const TubeTypes = require('../tubetypes')
const ytdl = require('ytdl-core');
const fs = require('fs')
const request = require('request');

class DataDownloader{

    /**
     * @param {DataHandler} datman 
     */
    constructor(datman){
        this.datman = datman;
        this.MAX_IMAGE_SIZE = 10/*MB*/*(1024*1024);
        this.thumbnail_sources = [
            "http://img.youtube.com/vi/<ID>/maxresdefault.jpg",
            "http://img.youtube.com/vi/<ID>/hqdefault.jpg",
            "http://img.youtube.com/vi/<ID>/mqdefault.jpg",
            "http://img.youtube.com/vi/<ID>/default.jpg",
        ];
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
                downloadThumbnail(data, info);
            }
            if(!data._hasAudio){
                downloadAudio(data, info);
            }
            if(!data._hasVideo){
                downloadVideo(data, info);
            }
            
        });
    }

    /**
     * Uses ytdl to get various meta information
     * currently meta informations contain:
     * Title
     * @param {TubeData} data 
     * @param {import('ytdl-core').videoInfo} ytdl_info 
     */
    downloadMetaInformation(data, ytdl_info){
        data.setMeta(ytdl_info.title);
    }


    /**
     * Uses ytdl to get thumbnail
     * Folder to data has to exist before calling this function
     * @param {TubeData} data 
     */
    downloadThumbnail(data){
        let path = this.datman.getDataFilePath(data, TubeTypes.FILE_IMAGE);
        this.findImageURL(data.id, function(url, size){
            if(!url) return;
            request(url)
                .pipe(fs.createWriteStream(path))
                .on('close', function(){
                    console.log("image downloaded");
                    data.setImage(size);
                });
        })
    }

    findImageURL(id, callback, sourceIndex){
        if(!sourceIndex) sourceIndex = 0;
        if(sourceIndex >= this.thumbnail_sources.length) return;

        let url = this.thumbnail_sources[sourceIndex].
            replace("<ID>", id);
        
        let max = this.MAX_IMAGE_SIZE;
        request.head(url, function(err, res, body){
            if(res.headers['content-type'].match("image") && res.headers['content-length'] < max && res.statusCode != 404){
                callback(url, res.headers['content-length']);
            } else {
                this.findImageURL(id, callback, sourceIndex+1);
                console.log("failed");
            }
        }.bind(this));
    }

    /**
     * Uses ytdl to get audio file
     * @param {TubeData} data 
     * @param {import('ytdl-core').videoInfo} ytdl_info 
     */
    downloadAudio(data, ytdl_info){
        //TODO
        //fs.appendFileSync(this.datman.getDataFilePath(data, TubeTypes.FILE_AUDIO)); SYNC OR NOT SYNC!
        //data.setAudio(size);
    }

    /**
     * Uses ytdl to get video file
     * @param {TubeData} data
     * @param {import('ytdl-core').videoInfo} ytdl_info 
     */
    downloadVideo(data, ytdl_info){
        let size;
        ytdl(data.id, {format: this.findBestVideoFormat(ytdl_info.formats)})
            .on('response', function(res){
                size = res.headers['content-length'];
            })
            .pipe(fs.createWriteStream(this.datman.getDataFilePath(data, TubeTypes.FILE_VIDEO)))
            .on('close', function(){
                console.log("video downloaded", size);
                data.setVideo(size);
            })
    }

    findBestVideoFormat(formats){
        let best = null;
        formats
            .filter(d => d.container == "mp4")
            .filter(d => Number.parseInt(d.quality_label) > 0)
            .forEach(d => {
                if(best == null){
                     best = d;
                }
                if(Number.parseInt(d.quality_label) > Number.parseInt(best.quality_label)){
                    best = d;
                    console.log("i choose you", Number.parseInt(d.quality_label));
                }
            });
        return best;
    }

}

module.exports = DataDownloader;
//For Docs
const TubeData = require('./tubedata');
const DataHandler = require('./datahandler');

const TubeTypes = require('../tubetypes')
const ytdl = require('ytdl-core');
const fs = require('fs')
const request = require('request');
const spawn = require('child_process').spawn;

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
                this.requestDownload(data[i].id);
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

        this.getYTDLandDownload(data);
    }

    getYTDLandDownload(data){
        ytdl.getInfo(data.id).then((info => {
            //check if info is valid?
            //what if bad id is handed
            this.datman.createTubeDataFolder(data);
            this.downloadMetaInformation(data, info);
            this.downloadThumbnail(data, info);
            this.downloadVideo(data, info);
            this.downloadAudio(data, info);
        }).bind(this));
    }

    /**
     * Uses ytdl to get various meta information
     * currently meta informations contain:
     * Title
     * @param {TubeData} data 
     * @param {import('ytdl-core').videoInfo} ytdl_info 
     */
    downloadMetaInformation(data, ytdl_info){
        if(data._hasMeta) return;
        this.datman.addTitle(data, ytdl_info.title);
    }


    /**
     * Uses ytdl to get thumbnail
     * Folder to data has to exist before calling this function
     * @param {TubeData} data 
     */
    downloadThumbnail(data){
        if(data._hasImage) return;
        let path = this.datman.getDataFilePath(data, TubeTypes.FILE_IMAGE);

        let context = this; //is this nice style?
        this._findImageURL(data.id, function(url, size){
            if(!url) return;
            request(url)
                .pipe(fs.createWriteStream(path))
                .on('close', function(){
                    console.log("image downloaded", size);
                    context.datman.addImage(data, size);
                });
        })
    }

    _findImageURL(id, callback, sourceIndex){
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
        if(data._hasAudio) return;
        if(!data._hasVideo){
            //possible race condition
            this.downloadVideo(data, ytdl_info);
            return;
        }
        console.log("hello", data._hasVideo, data.downloading);
        this._extractAudioFromVideo(data);
    }

    _extractAudioFromVideo(data){
        let path = this.datman.getTubeDataFolder(data)+ data.id
        let ffmpeg = spawn('ffmpeg', ['-i',  path + ".mp4", '-vn', path + ".m4a"]);
        ffmpeg.on('exit', (code => {
            if(code === 0){
                let size = fs.statSync(path + ".m4a").size;
                this.datman.addAudio(data, size)
                console.log("Audio done", size);
                data.setAudio(size);
            } else {
                console.log("Error on FFMPEG", code);
            }
        }).bind(this));
    }

    /**
     * Uses ytdl to get video file
     * after download finish extracts audio
     * @param {TubeData} data
     * @param {import('ytdl-core').videoInfo} ytdl_info 
     */
    downloadVideo(data, ytdl_info){
        if(data.downloading) return;
        data.downloading = true;
        if(data._hasVideo) return;
        let size;
        let f = this._findBestVideoFormat(ytdl_info.formats);
        //if(false)
        ytdl(data.id, {format: f})
            .on('response', function(res){
                size = res.headers['content-length'];
            })
            .pipe(fs.createWriteStream(this.datman.getDataFilePath(data, TubeTypes.FILE_VIDEO)))
            .on('close', function(){
                console.log("video downloaded", size);
                this.datman.addVideo(data, size);
                this._extractAudioFromVideo(data);
            }.bind(this))
    }

    /**
     * filters the given formats
     * should return a format with video and audio encoding
     * the downloadspeed can differ very strong from format to format
     * this function should pick the fastest one
     * (probably: 720p with H.264 )
     */
    _findBestVideoFormat(formats){
        let best = null;
        formats
            .filter(d => d.audioEncoding != null)
            .filter(d => d.type.match("video"))
            //.filter(d => d.container == "mp4")
            //.filter(d => Number.parseInt(d.quality_label) < 1080)
            .forEach(d => {
                if(best == null){
                     best = d;
                }
                if(Number.parseInt(d.quality_label) > Number.parseInt(best.quality_label)){
                    best = d;
                    console.log("i choose you", d);
                }

                console.log("f", d.container, d.encoding, d.type)
            });
            //console.log(best);
        return best;
    }

}

module.exports = DataDownloader;
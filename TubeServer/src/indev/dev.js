const fs = require('fs');
const request = require('request');
var http = require('http');
const ytdl = require('ytdl-core')
const spawn = require('child_process').spawn;

//Docs
const TubeCompanion = require('../tubecompanion');
const TubeData = require('../data/tubedata');

/**
 * @param {TubeCompanion} main 
 */
function dev(main){
    let id = "dOB1tw4r7Cg";
    let data = new TubeData(id);
    /*
    main.dataHan.createTubeDataFolder(data);
    ytdl.getInfo(id).then(info => {
        main.dwnldHan.downloadVideo(data, info);
        
    });
    */

    main.dwnldHan.requestDownload(id);
    

}

module.exports = dev;
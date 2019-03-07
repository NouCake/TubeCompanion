const fs = require('fs');
const request = require('request');
var http = require('http');
const ytdl = require('ytdl-core')

//Docs
const TubeCompanion = require('../tubecompanion');
const TubeData = require('../data/tubedata');

/**
 * @param {TubeCompanion} main 
 */
function dev(main){
    let id = "dOB1tw4r7Cg";

    
    ytdl.getInfo(id).then(info => {
        main.dwnldHan.downloadVideo(new TubeData(id), info);
        
    });
}

module.exports = dev;
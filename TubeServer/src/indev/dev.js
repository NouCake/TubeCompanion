const fs = require('fs');
const request = require('request');
var http = require('http');
const ytdl = require('ytdl-core')
const spawn = require('child_process').spawn;

//Docs
const TubeCompanion = require('../tubecompanion');
const TubeData = require('../data/tubedata');
const TubeTypes = require('../tubetypes')

/**
 * @param {TubeCompanion} main 
 */
function dev(main){
    let id = "7KF31dRMeos";
    let data = new TubeData(id);
    /*
    main.dataHan.createTubeDataFolder(data);
    ytdl.getInfo(id).then(info => {
        main.dwnldHan.downloadVideo(data, info);
        
    });
    */

    //main.dwnldHan.requestDownload(id);

    main.dataHan.getDataFile(data, TubeTypes.FILE_IMAGE, 1024,
        function(data){

        })
    

}

module.exports = dev;
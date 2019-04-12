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
    let id = "dOB1tw4r7Cg";
    let data = new TubeData(id);

    //main.dataHan.createTubeDataFolder(data);
    ytdl.getInfo(id).then(info => {
        main.dwnldHan.downloadVideo(data, info);
    });
    console.log("Hello");


    let path = main.dataHan.getDataFilePath(data, TubeTypes.FILE_IMAGE)
    if(false)
    main.dataHan.loader.readFileAsync(path, 1024, 
        function(buffer, offset, length){
            console.log(offset, length);
        });

    

}

module.exports = dev;
const EventFactory = require('./eventfactory');
const Packets = require('./packets');
const ytdl = require('ytdl-core')

//For Docs
const TubeCompanion = require('../../tubecompanion');

/**
 * @param {TubeCompanion} main
 */
module.exports = function(main){
    let event = EventFactory("yt");
    
    event.examplePacket = Packets.YTPacket;
    event.onEvent = function(socket, packet){
        let account = main.isAuthenticated(socket);
        if(!account) {
            console.log("not auth");
            return;
        }

        if(!ytdl.validateID(packet.link)){
            console.log("bad link");
            return;
        }

        account.addToSubmit(packet.link);
        main.dwnldHan.requestDownload(packet.link);
    }

    return event;
}
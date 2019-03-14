const EventFactory = require('./eventfactory');
const Packets = require('./packets');

//Doc
const TubeCompanion = require('../../tubecompanion');

/**
 * @param {TubeCompanion} main
 */
module.exports = function(main){
    let event = EventFactory("request");
    
    event.examplePacket = Packets.RequestPacket;
    event.onEvent = function(socket, packet){
        main.onRequest(socket, packet);
    }

    return event;
}
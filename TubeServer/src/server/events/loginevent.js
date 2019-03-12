const EventFactory = require("./eventfactory");
const Packets = require('./packets');
const TubeTypes = require('../../tubetypes');

//Doc
const TubeCompanion = require('../../tubecompanion');

/**
 * @param {TubeCompanion} main 
 */
function LoginEvent(main){
    let event = EventFactory("login");
    event.examplePacket = Packets.LoginPacket;
    event.onEvent = function(socket, packet){
        main.onLoginAttempt(socket, packet.apptype, packet.username, packet.password);
    }
    event.failed = function(socket){
        console.log(event.error);
        main.onLoginFailed(socket, event.error)
    }

    event.parsePacket = function(packet){
        try{
            return JSON.parse(packet);
        } catch(e){
            event.error = "Could not parse Packet."
            event.errorCode = TubeTypes.LOGIN_FAILED_BAD_PACKET;
            return null;
        }
    }
    return event;
}

module.exports = LoginEvent;
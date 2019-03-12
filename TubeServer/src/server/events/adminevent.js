const EventFactory = require("./eventfactory");

//Doc
const TubeCompanion = require('../../tubecompanion');

/**
 * 
 * @param {TubeCompanion} main 
 */
function AdminEvent(main){
    let event = EventFactory("admin");
    event.parse = false;
    event.checkPacket = () => true;
    event.onEvent = function(socket, msg){
        try{
            eval(msg);
        } catch(e){
            console.log(e);
        }
    }
    return event;
}

module.exports = AdminEvent;
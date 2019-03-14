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
            let val = eval(msg);
            if(event.return){
                socket.emit("message", val);
                event.return = false;
            }
        } catch(e){
            console.log(e);
        }
    }
    return event;
}

module.exports = AdminEvent;
const EventFactory = require("./eventfactory");

//Doc
const TubeCompanion = require('../../tubecompanion');

/**
 * 
 * @param {TubeCompanion} main 
 */
function DisconnectEvent(main){
    let event = EventFactory("disconnect");
    event.parse = false;
    event.checkPacket = () => true;
    event.onEvent = function(socket, packet){
        let account = main.accHan.findAccountBySocket(socket);
        if(account){
            main.onLogout(socket, account);
        } else {
            console.log(socket.id, "has no activ account");
        }
    }
    return event;
}

module.exports = DisconnectEvent;
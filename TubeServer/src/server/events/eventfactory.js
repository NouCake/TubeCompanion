const Socket = require('socket.io/lib/socket')

/**
 * Creates an Event-Object
 * if event.parse is set to false packets will be passed unparsed to onEvent
 * 
 * event.checkPacket and event.onEvent have to be implemented
 * 
 * if event.checkPacket is not implemented but event.examplePacket is set
 * event.checkPacket will compare received packet for all attributes in examplePacket
 * @param {String} tag 
 * @param {PacketHandler} pacHan 
 */
function EventFactory(tag){
    let event = {};

    event.tag = tag;
    event.error = "No Error!";
    event.parse = true;

    /**
     * Handles given event
     * will receive a parsed and valid packet
     * @param {Socket} socket
     * @param {Any} packet
     */
    event.onEvent = unimplemented;

    /**
     * returns true if given packet is valid for this eventhandler
     * packet will already be parsed to JSON 
     * @param {Any} packet
     * @returns {Boolean}
     */
    event.checkPacket = function(packet){
        if(event.examplePacket){
            if(!packet) return false;
            for(let attr in event.examplePacket){
                if(packet[attr] == null){
                    event.error = "Packet does not have Attribute " + attr;
                    return false;
                }
            }
            if(event.examplePacket.type != null && event.examplePacket.type != packet.type){
                event.error = "Packet does have wrong TypeID, (but all atributes are the same?? N00b)"
                return false;
            }
            return true;
        }
        unimplemented();
    };
    
    event.getHandler = function(socket){
        return function(packet){
            if(event.parse){
                packet = event.parsePacket(packet);
                if(!packet) {
                    event.failed(socket);
                    return;
                }
            }
            if(!event.checkPacket(packet)){
                event.failed(socket);
                console.log(packet);
                return;
            }
            event.onEvent(socket, packet);
        }
    }

    event.parsePacket = function(packet){
        try{
            return JSON.parse(packet);
        } catch(e){
            event.error = "Could not parse Packet."
            return null;
        }
    }

    /**
     * @param {Socket} socket
     */
    event.failed = function(socket){
        console.log("Error", event.error);
    }
    return event;
}

function unimplemented(){
    this.error = "Unimplemented function called";
    return false;
}

module.exports = EventFactory;
const SamplePackets = require('./samplepackets');
const TubeTypes = require('../tubetypes');
class PacketHandler{

    constructor(main, server, accHan){
        this.main = main;
        this.server = server;
        this.accHan = accHan;

        this.getEventHandler = this.getEventHandler.bind(this);
        this.error = "No Error found";
    }

    getEventHandler(socket, event){
        let handler = null;

        let hasEvent = false;
        for(let e in PacketHandler.Events){
            if(event == PacketHandler.Events[e]) hasEvent = true;
        }
        if(!hasEvent) {
            console.log("Unknown Event:", event);
            return;
        }


        //CamelCase: event => handleEvent
        event = "handle" + event.charAt(0).toUpperCase().concat(event.substr(1));
        handler = this[event].bind(this);
        return (packet => {handler(socket, packet)});
    }

    compareWithSamplePacket(packet, type){
        if(!packet){
            this.error = "Packet is null";
            return false;
        }

        let sample = SamplePackets.getByType(type);
        if(!sample){
            this.error = "Server has no record of a Packet with this type";
            return false;
        }
        for(let attr in sample){
            if(packet[attr] == null){
                this.error = "Attribute " + attr + " is null";
                return false;
            }
        }
        if(packet.type != sample.type){
            this.error = "Packet has wrong type";
            return false;
        }
        return true;
    }

    parsePacket(packet){
        try{
            return JSON.parse(packet);
        } catch(e){
            return null;
        }
    }

    handleLogin(socket, packet){
        if(!this.accHan.findAccountBySocket(socket)){
            packet = this.parsePacket(packet);
            if(!packet){
                sendLoginResponse(socket, TubeTypes.LOGIN_FAILED_BAD_PACKET);
                return;
            }
            if(this.compareWithSamplePacket(packet, TubeTypes.LOGIN)){
                this.main.onLoginAttempt(socket, packet.apptype, packet.username, packet.password);
            }
        } else {
            sendLoginResponse(socket, TubeTypes.LOGIN_FAILED_ACTIV_CONNECTION);
        }
    }
    
    handleDisconnect(socket, packet){
        let account = this.accHan.findAccountBySocket(socket);
        if(account){
            this.main.onLogout(socket, account);
        } else {
            console.log(socket.id, "has no activ account");
        }
    }

    sendLoginResponse(socket, res){
        let packet = Object.assign({}, SamplePackets.LoginResponsePacket);
        packet.res = res;
        this.server.sendPacket(socket, PacketHandler.Events.LOGIN, packet);
    }

}

PacketHandler.Events = {
    LOGIN: "login",
    DISCONNECT: "disconnect"
}

module.exports = PacketHandler;
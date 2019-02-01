//For Docs
const TubeCompanion = require('../tubecompanion');
const TubeServer = require('../server/tubeserver');
const AccountHandler = require('../account/accounthandler');
const Socket = require('socket.io/lib/socket');

//Needed Imports
const SamplePackets = require('./samplepackets');
const TubeTypes = require('../tubetypes');

class PacketHandler{

    /**
     * 
     * @param {TubeCompanion} main 
     * @param {TubeServer} server 
     * @param {AccountHandler} accHan 
     */
    constructor(main, server, accHan){
        this.main = main;
        this.server = server;
        this.accHan = accHan;

        this.getEventHandler = this.getEventHandler.bind(this);
        this.error = "No Error found";
    }

    /**
     * Returns a event handler function that is bind to this
     * @param {Socket} socket 
     * @param {String} event 
     * @returns {Function} event handler function
     */
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
        //           login => handleLogin
        event = "handle" + event.charAt(0).toUpperCase().concat(event.substr(1));
        handler = this[event].bind(this);
        return (packet => {handler(socket, packet)});
    }

    /**
     * Compares an Object with a sample packet by attributes
     * @param {Any} packet Any Object to be compares with given sample packet
     * @param {Number} type Type of SamplePacket
     */
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

    /**
     * Parses given object,
     * does not crash server if object cannot be parsed,
     * returns null in this case
     * @param {String} packet 
     */
    parsePacket(packet){
        try{
            return JSON.parse(packet);
        } catch(e){
            return null;
        }
    }

    /**
     * EVENT: LOGIN
     * handles a packet that is received on event listener
     * @param {Socket} socket socket which sent this packet
     * @param {SamplePackets.LoginPacket} packet (yet)unknown packet
     */
    handleLogin(socket, packet){
        if(!this.accHan.findAccountBySocket(socket)){
            packet = this.parsePacket(packet);
            if(!packet){
                sendLoginResponse(socket, TubeTypes.LOGIN_FAILED_BAD_PACKET);
                return;
            }
            if(this.compareWithSamplePacket(packet, TubeTypes.LOGIN)){
                this.main.onLoginAttempt(socket, packet.apptype, packet.username, packet.password);
            } else {
                this.server.sendMessage(socket, this.error);
            }
        } else {
            sendLoginResponse(socket, TubeTypes.LOGIN_FAILED_ACTIV_CONNECTION);
        }
    }
    

    /**
     * EVENT: DISCONNECT
     * handles a packet that is received on event listener
     * @param {Socket} socket socket which sent this packet
     */
    handleDisconnect(socket){
        let account = this.accHan.findAccountBySocket(socket);
        if(account){
            this.main.onLogout(socket, account);
        } else {
            console.log(socket.id, "has no activ account");
        }
    }


    /**
     * EVENT: YT
     * handles a packet that is received on event listener
     * @param {Socket} socket socket which sent this packet
     * @param {SamplePackets} packet (yet)unknown packet
     */
    handleYt(socket, packet){
        let account = this.accHan.findAccountBySocket(socket);

        if(!account){
            this.server.sendMessage(socket, "");
            return;
        }

    }

    /**
     * Sends a LoginResponsePacket to given socket
     * @param {Socket} socket 
     * @param {Number} res Response Type
     */
    sendLoginResponse(socket, res){
        let packet = Object.assign({}, SamplePackets.LoginResponsePacket);
        packet.res = res;
        this.server.sendPacket(socket, PacketHandler.Events.LOGIN, packet);
    }

}

PacketHandler.Events = {
    LOGIN: "login",
    DISCONNECT: "disconnect",
    YT: "yt"
}

module.exports = PacketHandler;
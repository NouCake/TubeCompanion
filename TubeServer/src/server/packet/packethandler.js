//For Docs
const TubeCompanion = require('../../tubecompanion');
const TubeServer = require('../tubeserver');
const AccountHandler = require('../../account/accounthandler');
const Socket = require('socket.io/lib/socket');

//Needed Imports
const SamplePackets = require('../events/packets');
const TubeTypes = require('../../tubetypes');

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
        switch(event){
            case PacketHandler.Events.DISCONNECT:
                handler = this.handleDisconnect;
                break;
            case PacketHandler.Events.LOGIN:
                handler = this.handleLogin;
                break;
            case PacketHandler.Events.YT:
                handler = this.handleYt;
                break;
            case PacketHandler.Events.REQUEST:
                handler = this.handleRequest;
                break;
        }
        if(!handler) {
            console.log("Unknown Event:", event);
            return;
        }
        
        handler = handler.bind(this);
        return packet => { handler(socket, packet) };
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

    handleRequest(socket, packet){
        let account = this.accHan.findAccountBySocket(socket);
        if(!account) {
            console.log("Unautorized");
            this.server.sendMessage(socket, "Unautorized");
            return;
        }

        packet = this.parsePacket(packet);
        if(!packet){ //Parsing error
            console.log("Bad Packet");
            this.server.sendMessage(socket, "Bad Packet");
            return;
        }

        if(!this.compareWithSamplePacket(packet, TubeTypes.REQUEST)){
            this.server.sendMessage(socket, this.error);
            return;
        }

        this.main.onRequest(account, packet);
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

module.exports = PacketHandler;
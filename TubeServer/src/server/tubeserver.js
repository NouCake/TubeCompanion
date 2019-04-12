//For Docs
const TubeCompanion = require('../tubecompanion');
const AccountHandler = require('../account/accounthandler');
const SocketIO = require('socket.io');
const Socket = require('socket.io/lib/socket');

//Needed Imports
const EventHandler = require('./eventhandler')
const PacketSender = require('./packetsender')

class TubeServer{
    
    /**
     * 
     * @param {TubeCompanion} main 
     * @param {SocketIO} io 
     * @param {AccountHandler} accHan 
     */
    constructor(main, io, accHan){
        this.main = main;
        this.ioServer = io;
        this.eventHandler = new EventHandler(main, this);
        this.packetSender = new PacketSender(this);

        this.onConnection = this.onConnection.bind(this);
        this.onDisconnect = this.onDisconnect.bind(this);
    }

    init(){
        this.ioServer.on('connection', this.onConnection);
    }

    /**
     * Registers all event handlers for given socket
     * @param {Socket} socket 
     */
    onConnection(socket){
        console.log("[", socket.id,"] has connected");
        this.eventHandler.registerUnprivilegedEvents(socket);
    }

    onConnectionAuthentificated(socket){
        this.eventHandler.registerPrivilegedEvents(socket);
    }

    /**
     * Weird function
     * creates and returns a handler function.
     * calls the eventhandler of packet handler but also prints the disconnection message to console
     * @param {Socket} socket 
     */
    onDisconnect(socket){
        const discHandler = this.pacHan.getEventHandler(socket, PacketHandler.Events.DISCONNECT);
        return function(packet){
            console.log("[", socket.id,"] has disconnected");
            discHandler(packet);
        }
    }

    /**
     * @returns {PacketHandler} returns packethandler
     */
    getPacketHandler(){
        return this.pacHan;
    }

    /**
     * send an packet to given socket
     * @param {Socket} socket socket which will receive packet
     * @param {String} event doesn't matter, since in current build all packets are sent on 'data' event
     * @param {Any} packet packet to send
     */
    sendPacket(socket, event, packet){
        if(socket) this.packetSender.sendPacket(socket, packet, event);
        else console.log("Tried to send Packet with invalid socket");
    }

    /**
     * Sends a message to the given socket
     * @param {Socket} socket 
     * @param {String} message 
     */
    sendMessage(socket, message){
        if(socket) socket.emit("message", message);
        else console.log("Tried to send Message with invalid socket");
    }

}

module.exports = TubeServer;
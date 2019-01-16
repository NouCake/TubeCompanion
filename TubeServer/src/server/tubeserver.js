const PacketHandler = require('../packet/packethandler');

class TubeServer{
    
    constructor(main, io, accHan){
        this.main = main;
        this.ioServer = io;

        this.pacHan = new PacketHandler(main, this, accHan);

        this.onConnection = this.onConnection.bind(this);
        this.onDisconnect = this.onDisconnect.bind(this);
    }

    init(){
        this.ioServer.on('connection', this.onConnection);
    }

    initSocket(socket){
        //socket.on('login');
    }

    onConnection(socket){
        console.log("[", socket.id,"] has connected");
        socket.on('login', this.pacHan.getEventHandler(socket, PacketHandler.Events.LOGIN));
        socket.on('disconnect', this.onDisconnect(socket));
    }

    onDisconnect(socket){
        const discHandler = this.pacHan.getEventHandler(socket, PacketHandler.Events.DISCONNECT);
        return function(packet){
            console.log("[", socket.id,"] has disconnected");
            discHandler(packet);
        }
    }

    getPacketHandler(){
        return this.pacHan;
    }

    sendPacket(socket, event, packet){
        socket.emit('data', packet);
    }

}

module.exports = TubeServer;
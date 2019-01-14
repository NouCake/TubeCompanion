class TubeServer{

    constructor(io){
        this.ioServer = io;
    }

    init(){
        this.ioServer.on('connection', this.onConnection.bind(this));
    }

    initSocket(socket){
        //socket.on('login');
    }

    onConnection(socket){

    }

    onDisconnection(socket){

    }

}

module.exports = TubeServer;
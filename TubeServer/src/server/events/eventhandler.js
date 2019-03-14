const LoginEvent = require('./loginevent');
const DisconnectEvent = require('./disconnectevent');
const AdminEvent = require('./adminevent');
const RequestEvent = require('./requestevent');
const YoutubeEvent = require('./youtubeevent');

//Doc
const TubeCompanion = require('../../tubecompanion');
const TubeServer = require('../tubeserver')
const Socket = require('socket.io/lib/socket')


class EventHandler {

    /**
     * 
     * @param {TubeCompanion} main 
     * @param {TubeServer} server 
     */
    constructor(main, server){
        this.main = main;
        this.server = server;

        this.loginEvent = LoginEvent(main);
        this.disconnectEvent = DisconnectEvent(main);
        this.adminEvent = AdminEvent(main);
        this.requestEvent = RequestEvent(main);
        this.youtubeEvent = YoutubeEvent(main);
    }

    /**
     * registers unprivileged events to given sockets.
     * unprivileged events are: Login, Disconnect
     * @param {Socket} socket 
     */
    registerUnprivilegedEvents(socket){
        this._registerEvent(socket, this.loginEvent);
        this._registerEvent(socket, this.disconnectEvent);
    }

    registerPrivilegedEvents(socket){
        this._registerEvent(socket, this.adminEvent);
        this._registerEvent(socket, this.requestEvent);
        this._registerEvent(socket, this.youtubeEvent);
    }

    _registerEvent(socket, event){
        socket.on(event.tag, event.getHandler(socket));
    }

}

module.exports = EventHandler;
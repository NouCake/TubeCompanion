//For Docs
const Socket = require('socket.io/lib/socket');
const Account = require('./account/account');


//Needed Imports
const TubeTypes = require('./tubetypes');
const TubeServer = require('./server/tubeserver');
const DataHandler = require('./data/datahandler');
const AccountHandler = require('./account/accounthandler');
const DownloadHandler = require('./data/datadownloader');
const LoginHandler = require('./loginhandler');

class TubeCompanion{
    
    constructor(ioserver){
        this.accHan = new AccountHandler();
        this.dataHan = new DataHandler(this);
        this.server = new TubeServer(this, ioserver, this.accHan);
        this.dwnldHan = new DownloadHandler(this.dataHan);
        this.loginHandler = new LoginHandler(this);
    }

    start(){
        this.dataHan.loadAllData();
        this.server.init();
    }

    onLoginAttempt(socket, apptype, username, password){
        this.loginHandler.onLogin(socket, apptype, username, password);  
    }

    /**
     * removes socket from given account
     * @param {Socket} socket 
     * @param {Account} account 
     */
    onLogout(socket, account){
        this.loginHandler.onLogout(socket, account);
    }

    /**
     * If given socket is authenticated (socket is logged into an account)
     * returns the connected account
     * otherwise returns null
     * @param {Socket} socket 
     * @returns {Account}
     */
    isAuthenticated(socket){
        return this.accHan.findAccountBySocket(socket);
    }

    /**
     * @param {Account} account 
     */
    sendPendingQueue(account, request){
        let packet = {};
        packet.type = TubeTypes.PENDING_DOWNLOAD_REQUESTS;
        packet.reqid = request.reqid;
        packet.ids = account.submitQueue;

        this.server.sendPacket(account.device, "data", packet);
    }

    /**
     * Requested TubeData has to exist and to be complete
     * @param {Account} account 
     * @param {String} id 
     * @param {Number} reqid 
     */
    sendRequestedMeta(account, id, reqid){
        let data = this.dataHan.getData(id);
        
        let packet = {};
        packet.type = TubeTypes.META_DATA;
        packet.reqid = reqid;
        packet.id = id;
        packet.title = data.title;

        this.server.sendPacket(account.device, "data", packet);
    }

    sendFile(socket, id, reqid, filetype){
        let data = this.dataHan.getData(id);
        let size = this.dataHan.getDataFileSize(data, filetype);
        let packet = {};

        packet.type = TubeTypes.FILE;
        packet.reqid = reqid;
        packet.id = id;
        packet.filetype = filetype;
        packet.filesize = size;
        
        //TODOs
        const bufferSize = 1024;
        const totalPackets = Math.floor((size+bufferSize-1)/bufferSize);

        packet.currentPacket = 0;
        packet.totalPackets = totalPackets;

        let context = this;
        this.dataHan.getDataFile(data, filetype, bufferSize, 
            function(buffer, offset, chunksize){
                let cp = {};
                Object.assign(cp, packet)
                cp.byteoffset = offset; //former: startbyte
                cp.data = buffer;
                cp.currentPacket = offset / bufferSize;
                cp.chunksize = chunksize;
                context.server.sendPacket(socket, "data", cp);
            })
        
    }

    onRequest(socket, packet){
        let account = this.isAuthenticated(socket);
        if(!account) {
            console.log("not auth");
            return;
        }
        
        switch(packet.reqtype) {
            case TubeTypes.REQUEST_PENDING:
                this.sendPendingQueue(account, packet);
                break;
            case TubeTypes.REQUEST_META:
                this.sendRequestedMeta(account, packet.id, packet.reqid);
                break;
            case TubeTypes.REQUEST_COVER: //old
            case TubeTypes.REQUEST_AUDIO: //old
            case TubeTypes.REQUEST_VIDEO: //old
            case TubeTypes.REQUEST_FILE:
                this.sendFile(socket, packet.id, packet.reqid, packet.filetype);
                break;
        }
    }

    getFileEndingByFiletType(filetype){
        switch(filetype){
            case TubeTypes.FILE_IMAGE:
                return TubeTypes.FILE_IMAGE;
            case TubeTypes.FILE_AUDIO:
                return TubeTypes.FILE_AUDIO;
            case TubeTypes.REQUEST_VIDEO:
                return TubeTypes.FILE_VIDEO;
        }
    }

    _dummyData(){

        this.dataHan.initData("1234");
        this.dataHan.initData("super");
        this.dataHan.initData("super");
        this.dataHan.addTitle("1234", "Africa by Toto");
        this.dataHan.addAudio("1234", 10);
        this.dataHan.addImage("1234", 10);

        this.dataHan.saveAllData();
    }

}

module.exports = TubeCompanion;
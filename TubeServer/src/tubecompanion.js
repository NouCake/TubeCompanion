//For Docs
const Socket = require('socket.io/lib/socket');
const Account = require('./account/account');

//Needed Imports
const TubeTypes = require('./tubetypes');
const TubeServer = require('./server/tubeserver');
const DataHandler = require('./data/datahandler');
const AccountHandler = require('./account/accounthandler');
const DownloadHandler = require('./data/datadownloader');

class TubeCompanion{
    
    constructor(ioserver){
        this.accHan = new AccountHandler();
        this.dataHan = new DataHandler(this);
        this.server = new TubeServer(this, ioserver, this.accHan);
        this.pacHan = this.server.getPacketHandler();
        this.dwnldHan = new DownloadHandler(this.dataHan);
    }

    start(){
        this.dataHan.loadAllData();
        this.server.init();
    }

    /**
     * Try to find an account with matching login-data and registers socket to account
     * @param {Socket} socket 
     * @param {Number} apptype 
     * @param {String} username 
     * @param {String} password 
     */
    onLoginAttempt(socket, apptype, username, password){
        let account = this.accHan.findAccountByUsername(username);
        let res = 0;
        if(!account){
            res = TubeTypes.LOGIN_FAILED_UNKNOWN_USER;

        } else if(!account.matches(username, password)){
            res = TubeTypes.LOGIN_FAILED_WRONG_PASSWORD;

        } else if(!account.loginSocket(socket, apptype)){
            res = TubeTypes.LOGIN_FAILED_ACTIV_CONNECTION;

        } else {
            res = TubeTypes.LOGIN_SUCCESS;
            console.log(`(${apptype}) ${account.username} has logged in`);
        }
        this.pacHan.sendLoginResponse(socket, res);
    }

    /**
     * removes socket from given account
     * @param {Socket} socket 
     * @param {Account} account 
     */
    onLogout(socket, account){
        const apptype = account.logoutSocket(socket);
        if(apptype){
            console.log(`(${apptype}) ${account.username} has logged out`);
        } else {
            console.log("Error, couldn't logout account");
        }
    }

    /**
     * @param {Account} account 
     * @param {Number} reqid 
     */
    sendPendingQueue(account, reqid){
        let packet = {};
        packet.type = TubeTypes.PENDING_DOWNLOAD_REQUESTS;
        packet.reqid = reqid;
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

    sendFile(account, id, reqid, filetype){
        let data = this.dataHan.getData(id);
        let size = this.dataHan.getDataFileSize(data, filetype);
        let packet = {};

        packet.type = TubeTypes.FILE;
        packet.reqid = reqid;
        packet.id = id;
        packet.filetype = filetype;
        //TODOs
        const bufferSize = 1024;
        const totalPackets = Math.floor((size+bufferSize-1)/bufferSize);

        packet.currentPacket = 0;
        packet.totalPackets = totalPackets;

        this.dataHan.getDataFile(data, filetype, bufferSize, 
            function(){

            })
        
    }

    onRequest(account, packet){
        switch(packet.reqtype) {
            case TubeTypes.REQUEST_PENDING:

            break;
            case TubeTypes.REQUEST_META:

            break;
            case TubeTypes.REQUEST_COVER:

            break;
            case TubeTypes.REQUEST_AUDIO:

            break;
            case TubeTypes.REQUEST_VIDEO:

            break;
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
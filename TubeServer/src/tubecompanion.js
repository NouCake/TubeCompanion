const TubeServer = require('./server/tubeserver');
const DataHandler = require('./data/datahandler');
const AccountHandler = require('./account/accounthandler');
const TubeTypes = require('./tubetypes');

class TubeCompanion{

    constructor(ioserver){
        this.accHan = new AccountHandler();
        this.dataHan = new DataHandler(this);
        this.server = new TubeServer(this, ioserver, this.accHan);
        this.pacHan = this.server.getPacketHandler();
    }

    start(){
        this.dataHan.loadAllData();
        this.server.init();
    }

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

    onLogout(socket, account){
        const apptype = account.logoutSocket(socket);
        if(apptype){
            console.log(`(${apptype}) ${account.username} has logged out`);
        } else {
            console.log("Error, couldn't logout account");
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
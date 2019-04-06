const TubeTypes = require('./tubetypes');

//Doc
const TubeComapnion = require('./tubecompanion')

class LoginHandler {

    /**
     * @param {TubeComapnion} main 
     */
    constructor(main){
        this.main = main;
    }

    onLogin(socket, apptype, username, password){
        let res = 0;

        let account = this.main.accHan.findAccountBySocket(socket);
        if(account){
            res = TubeTypes.LOGIN_FAILED_ACTIV_CONNECTION;
        }
        
        account = this.main.accHan.findAccountByUsername(username);
        if(!account){
            res = TubeTypes.LOGIN_FAILED_UNKNOWN_USER;

        } else if(!account.matches(username, password)){
            res = TubeTypes.LOGIN_FAILED_WRONG_PASSWORD;

        } else if((res = account.loginSocket(socket, apptype)) != true){  //Terrible line of code, suffer future me!
            //res = TubeTypes.LOGIN_FAILED_ACTIV_CONNECTION;
            //Not neccessary since res get set in if
        } else {
            res = TubeTypes.LOGIN_SUCCESS;
            this.main.server.onConnectionAuthentificated(socket);
            console.log(`(${apptype}) ${account.username} has logged in`);
        }

        this.sendLoginResponse(socket, res);
    }

    onLogout(socket, account){
        const apptype = account.logoutSocket(socket);
        if(apptype){
            console.log(`(${apptype}) ${account.username} has logged out`);
        } else {
            console.log("Error, couldn't logout account");
        }
    }

    sendLoginResponse(socket, res){
        let packet = {};
        packet.type = TubeTypes.LOGIN_RESPONSE;
        packet.res = res;
        this.main.server.sendPacket(socket, "login", packet);
    }

}

module.exports = LoginHandler;  
const TubeTypes = require('../tubetypes');

class LoginPacket{
    
    /**
     * 
     * @param {Number} type 
     * @param {String} username 
     * @param {String} password 
     * @param {Number} apptype 
     */
    constructor(username, password, apptype){
        this.type = TubeTypes.LOGIN;
        this.username = username;
        this.password = password;
        this.apptype = apptype;
    }
}

class LoginResponsePacket{

    /**
     * @param {Number} res Response type
     */
    constructor(res){
        this.type = TubeTypes.LOGIN_RESPONSE;
        this.res = res;
    }

}

class Packets {

    constructor(){
        this.LoginPacket = LoginPacket;
        this.LoginResponsePacket = LoginResponsePacket;

        this.sampleLoginPacket = new LoginPacket("DEFAULT", "DEFAULT", TubeTypes.DEFAULT_VALUE);
        this.sampleLoginResponsePacket = new LoginResponsePacket(TubeTypes.DEFAULT_VALUE);
    }

    /**
     * returns a sample packet by type
     * @param {Number} type 
     */
    getByType(type){
        switch(type){
            case TubeTypes.LOGIN:
                return this.sampleLoginPacket;
            case TubeTypes.LOGIN_RESPONSE:
                return this.sampleLoginResponsePacket;
            default:
                return null;
        }
    }

}

module.exports = new Packets();
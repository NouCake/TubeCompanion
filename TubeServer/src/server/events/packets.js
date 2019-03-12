const TubeTypes = require('../../tubetypes');

const Packets = {};
Packets.LoginPacket = {
    type: TubeTypes.LOGIN,
    username: true,
    password: true,
    apptype: true
}

module.exports = Packets;

class RequestPacket{
        
    /**
     * 
     * @param {Number} reqid 
     * @param {Number} reqtype 
     */
    constructor(reqid, reqtype){
        this.type = TubeTypes.REQUEST;
        this.reqid = reqid;
        this.reqtype = reqtype;
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
const TubeTypes = require('../tubetypes');

module.exports = {
    getByType: function(type){
        switch(type){
            case TubeTypes.LOGIN:
                return this.LoginPacket;
            case TubeTypes.LOGIN_RESPONSE:
                return this.LoginResponsePacket;
            default:
                return null;
        }
    },
    LoginPacket: {
        type: TubeTypes.LOGIN,
        username: TubeTypes.DEFAULT_VALUE,
        password: TubeTypes.DEFAULT_VALUE,
        apptype: TubeTypes.DEFAULT_VALUE
    },
    LoginResponsePacket: {
        type: TubeTypes.LOGIN_RESPONSE,
        res: TubeTypes.DEFAULT_VALUE
    }
}
const TubeTypes = require('../../tubetypes');

const Packets = {};
Packets.LoginPacket = {
    type: TubeTypes.LOGIN,
    username: true,
    password: true,
    apptype: true
}

Packets.RequestPacket = {
    type: TubeTypes.REQUEST,
    reqid: true,
    reqtype: true
}

Packets.YTPacket = {
    type: TubeTypes.YT,
    link: true
}

Packets.LoginResponePacket = {
    type: TubeTypes.LOGIN_RESPONSE,
    res: true
}

module.exports = Packets;
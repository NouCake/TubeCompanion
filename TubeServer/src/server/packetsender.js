
//Doc
const TubeServer = require('./tubeserver'); 

//Consts
const MAX_SENDING = 25; //Per second

class PacketSender{

    /**
     * @param {TubeServer} server 
     */
    constructor(server){
        this.server = server;
        this.queue = [];

        this._loop_low = this._loop_low.bind(this)
        this._loop_high = this._loop_high.bind(this);
        this.timerID = setInterval(this._loop_low, 1000);
    }

    _loop_low(){
        if(this._getLength() > 0){
            clearInterval(this.timerID);
            this.timerID = setInterval(this._loop_high, 100);
            console.log("switched to high");
            this._sendSomePackets();
        }   
    }

    _loop_high(){
        if(this._getLength() <= 0){
            clearInterval(this.timerID);
            this.timerID = setInterval(this._loop_low, 1000);
            console.log("switched to low");
        } else {
            this._sendSomePackets();
        }
    }

    //It's hard to think of some good names...
    _sendSomePackets(){
        let count = this._getLength();
        if(count > 0){
            count = count > MAX_SENDING ? MAX_SENDING : count;
            for(let i = 0; i < count; i++){
                this._getNext().send();
            }
        }
    }

    sendPacket(socket, packet, tag){
        this._addRequest(new SendRequest(socket, packet, tag));
    }

    _addRequest(packet){
        this.queue.push(packet);
    }

    /**
     * @returns {SendRequest}
     */
    _getNext(){
        return this.queue.shift();
    }

    _getLength(){
        return this.queue.length;
    }

}

module.exports = PacketSender;

class SendRequest{

    constructor(socket, packet, tag){
        this.socket = socket;
        this.packet = packet;
        this.tag = tag;

        this.time = Date.now();
    }

    send(){
        this.socket.emit(this.tag, this.packet);
    }

}
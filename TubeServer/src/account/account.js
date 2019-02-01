//For Docs
const Socket = require('socket.io/lib/socket');

//Needed Imports
const TubeTypes = require('../tubetypes');

class Account {
    
    /**
     * 
     * @param {String} id 
     * @param {String} username 
     * @param {String} password 
     */
    constructor(id, username, password){
        this.id = id;
        this.username = username;
        this.password = password;

        this._hasClient = false;
        this._hasDevice = false;
        
        /**
         * Client Socket, is filled only when client is connected
         * @type {Socket}
         */
        this.client = null;
        
        /**
         * Device Socket, is filled only when device is connected
         * @type {Socket}
         */
        this.device = null;

        this._error = "";
    }
    
    /**
     * 
     * @param {String} username 
     * @param {String} password 
     * @returns {Boolean} true if login is matching
     */
    matches(username, password){
      username = username.toLowerCase().trim();
      let thisname = this.username.toLowerCase().trim();

      return thisname == username && this.password == password;
    }

    /**
     * 
     * @param {Socket} socket 
     * @param {Number} apptype 
     */
    loginSocket(socket, apptype){
        switch(apptype){
            case TubeTypes.LOGIN_DEVICE:
                return this._addDevice(socket);
            case TubeTypes.LOGIN_CLIENT:
                return this._addClient(socket);
            default:
                console.log("unknown apptype");
                return false;
        }
    }

    /**
     * 
     * @param {Socket} socket 
     */
    logoutSocket(socket){
        if(this.hasClient()){
            if(this.client.id == socket.id){
                this._removeClient();
                return TubeTypes.LOGIN_CLIENT;
            }
        }
        if(this.hasDevice()){
            if(this.device.id == socket.id){
                this._removeDevice();
                return TubeTypes.LOGIN_DEVICE;
            }
        }
        return false;
    }

    /**
     * 
     * @param {Socket} socket 
     */
    hasSocket(socket){
        if(socket){
            return (this.hasDevice() && socket.id == this.device.id) || 
            (this.hasClient() && socket.id == this.client.id);
        }
        return false;
    }

    hasDevice(){
        //_hasDevice XOR device
        if((this._hasDevice && !this.device) || (!this._hasDevice && this.device != null)){
            console.log("Inconsistent Account state - Please Debug\n" , this);
        }
        return this._hasDevice;
    }

    hasClient(){
        //_hasClient XOR client
        if((this._hasClient && !this.client) || (!this._hasClient && this.client != null)){
            console.log("Inconsistent Account state - Please Debug\n" , this);
        } 
        return this._hasClient;
    }

    _addDevice(socket){
        if(this.hasDevice()){
            this._error = "(DEVICE) " + this.username + " is alreaddy logged in";
            return false;
        }

        this.device = socket;
        this._hasClient = true;
        return true;
    }

    _removeDevice(){
        this.device = null;
        this._hasDevice = false;
    }

    _addClient(socket){
        if(this.hasClient()){
            this._error = "(CLIENT) " + this.username + " is alreaddy logged in";
            return false;
        }
        this.client = socket;
        this._hasClient = true;
        return true;
    }

    _removeClient(){
        this.client = null;
        this._hasClient = false;
    }

}

module.exports = Account;
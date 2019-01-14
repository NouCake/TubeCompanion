class Account {
    
    constructor(id, username, password){
        this.id = id;
        this.username = username;
        this.password = password;
    
        this._hasClient = false;
        this._hasDevice = false;
        this.client = null;
        this.device = null;

        this._error = "";
    }
  
    matches(username, password){
      username = username.toLowerCase().trim();
      let thisname = this.username.toLowerCase().trim();

      return thisname == username && this.password == password;
    }

    loginSocket(socket, apptype){
        
    }

    hasDevice(){
        //_hasDevice XOR device
        if((this._hasDevice && !this.device) || (!this._hasDevice && this.device)){
            console.log("Inconsistent Account state - Please Debug\n" , this);
        }
        return this._hasDevice
    }

    hasClient(){
        //_hasClient XOR client
        if((this._hasClient && !this.client) || (!this._hasClient && this.client)){
            console.log("Inconsistent Account state - Please Debug\n" , this);
        }
        return this._hasClient
    }

    _addDevice(socket){
        if(this.hasDevice()){
            this._error = "(DEVICE) " + username + " is alreaddy logged in";
            return false;
        }

        this.device = socket;
    }

}
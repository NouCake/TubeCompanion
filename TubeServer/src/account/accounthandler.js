const Account = require('./account');

class AccountHandler{

    constructor(){
        this._accounts = [];

        this.addAccount(new Account(0, "NouCake", "123456"))
    }

    addAccount(account){
        this._accounts.push(account);
    }

    findAccountBySocket(socket){
        let account = this._accounts.filter(acc => acc.hasSocket(socket));
        switch(account.length){
            case 0:
                return null;
            case 1:
                return account[0];
            default:
                console.log("Error: Multiple Accounts with same socket found");
                return null;
        }
    }

    findAccountByUsername(username){
        username = username.toLowerCase().trim();
        return this._accounts
            .filter(acc => acc.username.toLowerCase().trim() == username)[0];
    }

}

module.exports = AccountHandler;
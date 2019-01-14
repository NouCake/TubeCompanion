const TubeServer = require('./server/tubeserver');
const DataManager = require('./data/datamanager');

class TubeCompanion{

    constructor(ioserver){
        this.server = new TubeServer(ioserver);
        this.dataman = new DataManager(this);

        this.dataman.loadAllData();
        this.server.init();

        //this.dummyData();
    }

    dummyData(){

        this.dataman.initData("1234");
        this.dataman.initData("super");
        this.dataman.initData("super");
        this.dataman.addTitle("1234", "Africa by Toto");
        this.dataman.addAudio("1234", 10);
        this.dataman.addImage("1234", 10);

        this.dataman.saveAllData();
    }

}

module.exports = TubeCompanion;
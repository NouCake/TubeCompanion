const fs = require('fs')

class DataLoader{

    constructor(){

    }

    createFolder(path){
        if(!fs.existsSync(path)){
            fs.mkdirSync(path);
        }
    }

    createFile(path, content){
        if(!fs.existsSync(path)){
            fs.appendFileSync(path, content,
            err => {
                console.log("Error while creating data folder\n", err);
            });
        }
    }

    writeFile(path, content){
        fs.writeFileSync(path, content,
        (err) => {
            console.log("An error accoured while saving:\n",err);
        });
    }

    readFile(path, parsed){
        let data = fs.readFileSync(path, 'utf8');
        if(!parsed) return data;

        try{
            data = JSON.parse(data);
            return data;
        } catch(e){
            console.log("Error: Index file has been corrupted\n",e);
            return null;
        }
    }

    readFileAsync(path, chunksize, callback){
        console.log("was here");
        let buffer;
        fs.createReadStream(path)
            .on('data', data => {
                let offset = 0;
                while(offset + chunksize < data.length){
                    callback(data.slice(offset, offset + chunksize), offset, chunksize);
                    offset += chunksize;
                }
                callback(data.slice(offset, data.length), offset, data.length - offset);
            })
    }

}

module.exports = DataLoader;
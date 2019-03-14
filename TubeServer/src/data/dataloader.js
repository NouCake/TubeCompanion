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

    /**
     * 
     * @param {String} path 
     * @param {Number} chunksize 
     * @param {Function} callback 
     */
    readFileAsync(path, chunksize, callback){
        let buffer;
        let size = fs.statSync(path).size;
        let totalBytesRead = 0;
        fs.createReadStream(path)
            .on('data', data => {
                //old
                if(false){
                    let offset = 0;
                    while(offset + chunksize < data.length){
                        callback(data.slice(offset, offset + chunksize), offset, chunksize);
                        offset += chunksize;
                    }
                    callback(data.slice(offset, data.length), offset, data.length - offset);
                }

                //new
                if(true){
                    if(buffer) data = Buffer.concat([buffer, data]);
                    let offset = 0;
                    while(offset + chunksize < data.length){
                        callback(data.slice(offset, offset + chunksize), totalBytesRead + offset, chunksize);
                        offset += chunksize;
                    }
                    totalBytesRead += offset;
                    buffer = data.slice(offset, data.length);
                }
            })
            .on('close', () => {
                if(totalBytesRead < size){
                    //assert buffer.length <= chunksize
                    if(buffer.length > chunksize) {
                        console.log("fatal error while reading");
                        return;
                    }
                    callback(buffer, totalBytesRead, size - totalBytesRead);
                }
            })

    }

}

module.exports = DataLoader;
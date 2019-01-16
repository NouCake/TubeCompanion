var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

const TubeCompanion = require('./src/tubecompanion');

app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

http.listen(12012, function(){
    console.log('Listening on Port 12012');
});

const tube = new TubeCompanion(io);
tube.start();
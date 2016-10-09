var config = require('./config');
var express = require('express');
var mongodb = require('mongodb');
    
var app = express();
var mongoClient = mongodb.MongoClient;
var PORT = 8080;

var allowCrossDomain = function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Content-Type, X-XSRF-TOKEN');

    next();
}

app.use(express.bodyParser());
app.use(allowCrossDomain);

database = null;
mongoClient.connect(config.MongoUrl, function (err, db) {
    if (err)
        console.log(err);
    else 
        database = db
});

require('./Controller/ViewController.js')(app);
require('./Controller/UserController.js')(app);
require('./Controller/ImageController.js')(app);

app.listen(PORT);  
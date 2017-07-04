var http = require('http');
var fs = require('fs');
var workDir = process.env['WORK_DIR'];
if (!workDir) {
    throw new Error('No environment variable WORK_DIR defined');
}

var fnModule = require(process.env['WORK_DIR'] + '/function.js');
if (!fnModule['function']) {
    throw new Error('No export named \'function\' found');
}
var fn = fnModule['function'];
var config = extend(
    {
        contentType: 'text/plain'
    },
    fnModule.config || {});

http.createServer(function (req, res) {
    var body = [];
    req.on('data', function(chunk) {
        body.push(chunk);
    }).on('end', function() {
        body = Buffer.concat(body).toString();
        fn({
            headers: req.headers,
            method: req.method,
            url: req.url,
            body: body
        }, function(result) {
            console.log(result);
            res.writeHead(200, {'Content-Type': config.contentType || 'text/plain'});
            res.end(JSON.stringify(result));
        });
    });
}).listen(5000);

function extend(target) {
    var sources = [].slice.call(arguments, 1);
    sources.forEach(function (source) {
        for (var prop in source) {
            target[prop] = source[prop];
        }
    });
    return target;
}

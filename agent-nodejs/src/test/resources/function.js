/*

*/
module.exports.config = {
    contentType: 'application/json'
};

/*
Echo input
*/
module.exports['function'] = function(input, callback) {
    callback(input);
}

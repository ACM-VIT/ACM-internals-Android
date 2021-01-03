var finder = require('findit')('.');
var path = require('path');
var secrets = process.argv.slice(2);

console.log(secrets);

finder.on('file', function (file, stat) {
    if (secrets.includes(path.basename(file))){
        console.error(path.basename(file));
        throw new Error('Found secrets in commit')
    }
});

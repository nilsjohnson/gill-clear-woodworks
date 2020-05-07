const fs = require('fs');

/*
Writes object to file as JSON
*/
function writeObj(obj, name) {
	fs.writeFile(name, JSON.stringify(obj, null, 2), function (err) {
		if (err) {
			console.log(error)
		}
	});
}

/*
Reads JSON object from file and returns it
*/
function readObj(name) {
    if(fs.existsSync(name)) {
        let obj = JSON.parse(fs.readFileSync(name));
	    return obj;
    }
    console.log("File did not exist. Returning null");
	return null;
}

/*
deletes a file
*/
function deleteFile(path) {
	console.log("Path: ", path);
	fs.unlink(path, (err) => {
		if (err) {
			console.log("Problem deleting file: " + err);
		}
		else {
			console.log("delete sucess!");
		}
	});
}


exports.readObj = readObj;
exports.writeObj = writeObj;
exports.deleteFile = deleteFile;
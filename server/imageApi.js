const fs = require('fs');
const { app } = require("./server");
const bodyParser = require('body-parser');
const fileUtil = require('./fileUtil.js');
const busboy = require('connect-busboy');
const path = require('path');
const shell = require('shelljs');

app.use(bodyParser.json());
app.use(busboy())

const CAROUSEL_IMG_FILE_NAME = 'carousel.json';
let carouselImages = null;

const CAROUSEL_IMG_WIDTH = 900;
const CAROUSEL_IMG_HIGHT = 600;

/**
 * Loads the carousel images from the file
 * must be called on start
 */
function loadCarouselImages() {
	carouselImages = fileUtil.readObj(CAROUSEL_IMG_FILE_NAME);
	
	if(carouselImages == null) {
		carouselImages = [];
	}
}

function addCarouselImage(image) {
	carouselImages.push(image);
	fileUtil.writeObj(carouselImages, CAROUSEL_IMG_FILE_NAME);
}

function getCarouselImages() {
	return carouselImages;
}

const { spawn } = require('child_process');

/*
API To handle carousel uploads
*/
app.post('/api/carouselUpload', function (req, res, next) {
	console.log("image upload api hit");
	let fstream;
	req.pipe(req.busboy);
	req.busboy.on('file', function (fieldname, file, filename) {
		console.log("Uploading: " + filename);
		let src = '/tmp/' + filename;
		let dst = path.join(__dirname, '../public/carouselImg/' + filename);
		let finalAddr = '/carouselImg/' + filename;

		// does not allow duplicate titles
		let i = 2;
		while (carouselImages.includes(finalAddr)) {
			console.log("exists");
			filename = "(" + i + ")" + filename;
			src = '/tmp/' + filename;
			dst = path.join(__dirname, '../public/carouselImg/' + filename);
			finalAddr = '/carouselImg/' + filename;
			i++;
		}
		//Path where image will be uploaded
		fstream = fs.createWriteStream(src);
		file.pipe(fstream);
		fstream.on('close', function () {
			console.log("'" + filename + "' uploaded..resize next");
			resize(src, dst, CAROUSEL_IMG_WIDTH, CAROUSEL_IMG_HIGHT, function (destination) {
				res.json({ address: finalAddr });
				addCarouselImage(finalAddr);
				shell.rm(src); // delete temporary file
			});
		});
	});
});

app.get('/api/carouselImages', function(req, res) {
    console.log("get image api hit: ");
	res.json(carouselImages);

});

/*
API to shift carousel images left or right
*/
app.put('/api/carouselImgs/move', function (req, res) {
	let result;
	try {
		moveCarouselImage(req.body.direction, req.body.image);
		result = "Image moved."
	}
	catch (err) {
		console.log(err);
		result = err;
		res.status(400);
	}

	res.json({ result: result });
});

/*
API to handle carousel image deletes
*/
app.delete('/api/carouselImgDelete', function (req, res) {
	let result
	console.log(req.body.image);
	try {
		deleteImage(req.body.image);
		result = "Delete Success!"; // TODO, just send the code..
	}
	catch (err) {
		console.log(err);
		res.status(400);
		result = "Delete Failed."
	}

	res.json({ result: result });
});

function deleteImage(image) {
	for(let i = 0; i < carouselImages.length; i++) {
		if(carouselImages[i] === image) {
			carouselImages.splice(i, 1);
			let imgPath = path.join(__dirname, '../public/' + image);
			fileUtil.writeObj(carouselImages, CAROUSEL_IMG_FILE_NAME);
			fileUtil.deleteFile(imgPath);
			return;
		}
	}

	throw("No image to remove.");
}

/*
moves an image
*/
function moveCarouselImage(direction, image) {
	let imageIndex = 0;
	let found = false;

	while (imageIndex < carouselImages.length) {
		if (carouselImages[imageIndex] === image) {
			found = true;
			break;
		}
		else {
			imageIndex++;
		}
	}

	if (found) {
		let rightIndex, leftIndex, temp;

		if (imageIndex === 0) {
			leftIndex = carouselImages.length - 1;
			rightIndex = imageIndex + 1;
		}
		else if (imageIndex === carouselImages.length - 1) {
			rightIndex = 0;
			leftIndex = imageIndex - 1;
		}
		else {
			rightIndex = imageIndex + 1;
			leftIndex = imageIndex - 1;
		}

		temp = carouselImages[imageIndex];
		if (direction === "RIGHT") {
			carouselImages[imageIndex] = carouselImages[rightIndex];
			carouselImages[rightIndex] = temp;
		}
		if (direction === "LEFT") {
			carouselImages[imageIndex] = carouselImages[leftIndex];
			carouselImages[leftIndex] = temp;
		}
		// save this ordering
		fileUtil.writeObj(carouselImages, CAROUSEL_IMG_FILE_NAME);
	}
	else {
		console.log("Can't move image. Image not found");
	}
}

/*
To resize images. Spawns a Java program. 
	-Will take src image, crop to proper aspect ratio and resize.
	-Sections of edge may be lost, but image will not appear "smooshed"
*/
function resize(src, dst, width, height, callback) {
	//let args = ['Resize', src, dest, width, height];
	console.log("src: " + src);
	console.log("dst: " + dst);
	let dim = width + 'x' + height;
	console.log("dim: " + dim);


	let args = [src, "-resize", dim, dst];
	const resizer = spawn('convert', args);

	resizer.stdout.on('data', (data) => {
		console.log(`stdout: ${data}`);
	});

	resizer.stderr.on('data', (data) => {
		console.log(`stderr: ${data}`);
	});

	resizer.on('close', (code) => {
		//console.log(`child process exited with code ${code}.`);
		if (code === 0) {
			console.log("Image was resized.");
			callback(dst)
		}
		else {
			console.log("Image not resized.");
		}
	});
}


loadCarouselImages();


const fs = require('fs');
const bodyParser = require('body-parser');
const busboy = require('connect-busboy');
const path = require('path');
const shell = require('shelljs');
const { spawn } = require('child_process');

const { app } = require("./server");
const fileUtil = require('./fileUtil.js');

app.use(bodyParser.json());
app.use(busboy())


const CAROUSEL_IMG_FILE_NAME = 'carousel.json';
const CAROUSEL_IMAGE_DIR = path.join(__dirname, '../public/');
const PUBLIC_PATH = "carouselImg/";
let carouselImages = null;
const CAROUSEL_IMG_WIDTH = 900;
const CAROUSEL_IMG_HIGHT = 600;
const LEFT = "LEFT", RIGHT = "RIGHT";

/**
 * Reads the carousel images from disk. After calling, carouselImages will
 * not be null. 
 */
function loadImages() {
	carouselImages = fileUtil.readObj(CAROUSEL_IMG_FILE_NAME);
	if(carouselImages == null) {
		carouselImages = [];
	}
}

/**
 * Returns the carousel image array, or an empty array if there are none.
 */
function getCarouselImages() {
	if(carouselImages == null) {
		loadImages();
	}

	return carouselImages;
}

/**
 * @param {string} image Image to add to carousel, i.e "fileName.jpg"
 */
function addCarouselImage(image) {
	carouselImages.push(image);
	fileUtil.writeObj(carouselImages, CAROUSEL_IMG_FILE_NAME);
}

/**
 * @param {string} image File name of image to delete. I.e 'filename.jpg' 
 */
function deleteImage(image) {
	for(let i = 0; i < getCarouselImages().length; i++) {
		if(getCarouselImages()[i] === image) {
			getCarouselImages().splice(i, 1);
			let imgPath = CAROUSEL_IMAGE_DIR + image;
			fileUtil.writeObj(getCarouselImages(), CAROUSEL_IMG_FILE_NAME);
			fileUtil.deleteFile(imgPath);
			return;
		}
	}

	throw("No image to remove.");
}

/**
 * 
 * @param {number} direction Left or right. 
 * @param {string} image Filename of image. 
 */
function moveCarouselImage(direction, image) {
	let imageIndex = 0;
	let found = false;

	// we directly access carousel images, so we make sure its loaded.
	loadImages();

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
		if (direction === RIGHT) {
			carouselImages[imageIndex] = carouselImages[rightIndex];
			carouselImages[rightIndex] = temp;
		}
		if (direction === LEFT) {
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
API endpoint To handle carousel uploads
*/
app.post('/api/carouselUpload', function (req, res, next) {
	console.log("image upload api hit");
	let fstream;
	req.pipe(req.busboy);
	req.busboy.on('file', function (fieldname, file, filename) {
		console.log(`Uploading File: ${filename}`);
		let src = '/tmp/' + filename;
		let dst = CAROUSEL_IMAGE_DIR + PUBLIC_PATH + filename;
		let saveName = PUBLIC_PATH + filename;

		// rename images as to not overwrite files with same name.
		let i = 2;
		while (getCarouselImages().includes(saveName)) {
			saveName = PUBLIC_PATH + "(" + i + ")" + filename;
			console.log(`Duplicate filename found. Renaming it "${saveName}"`);
			dst = CAROUSEL_IMAGE_DIR + saveName;
			i++;
		}
		//Path where image will be uploaded
		fstream = fs.createWriteStream(src);
		file.pipe(fstream);
		fstream.on('close', function () {
			console.log("'" + filename + "' uploaded..resize next");
			resize(src, dst, CAROUSEL_IMG_WIDTH, CAROUSEL_IMG_HIGHT, function (destination) {
				res.json({ address: saveName });
				addCarouselImage(saveName);
				shell.rm(src); // delete temporary file TODO use fileUtil
			});
		});
	});
});

/**
 * API endpoint to get the carousel images
 */
app.get('/api/carouselImages', function(req, res) {
	console.log("get image api hit: ");
	res.json(getCarouselImages());

});

/*
API endpoint to shift carousel images left or right
*/
app.put('/api/carouselImgs/move', function (req, res) {
	console.log(req.body);
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


// get the image elements
var images = document.getElementsByClassName('img-thumbnail');
// array to hold the image sources
var locations = [];

// put the image sources into the array
for(var i = 0; i < images.length; i++)
{
	locations[i] = images[i].src;
	console.log(locations[i]);
}

// make the lightbox
lightBox = new LightBox(locations, "modal-image");

// helper functions to change lightbox
function setCurrentImage(obj)
{
	lightBox.setCurrentImage(obj);
}

function nextImage()
{
	lightBox.nextImage();
}

function previousImage()
{
	lightBox.previousImage();
}
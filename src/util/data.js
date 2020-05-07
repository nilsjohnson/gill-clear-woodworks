/*
For blogger API
*/
const key = "AIzaSyDyOzcQFTCjSmlT-nd5rbLXLca3glNkZhg";
// use google id for testing: 3213900
const userId = "4537747483030928219";

/*
gets the most recent blog posts
*/
function getLatest(maxResults) {
	let url = 'https://www.googleapis.com/blogger/v3/blogs/' + userId 
	 + '/posts?key=' + key 
	 + '&fetchBodies=false'
	 + '&fetchImages=true';
	
	if(maxResults > 0) {
		url += '&maxResults=' + maxResults;
	}
	
	return fetch(url);
}

/*
gets a post by its id
*/
function getPostById(postId) {
	return fetch('https://www.googleapis.com/blogger/v3/blogs/' + userId + '/posts/' + postId + '?key=' +key);
}

function getAbout()
{
	return fetch('/about');
}

function getCarouselImages() {
    return fetch('/api/carouselImages');
}

/*
upload a file
*/
function uploadFile(url = '', data = {}) {
    return fetch(url, {
        method: 'POST',
        body: data
    });
}


/*
post generic data to server
*/
function postData(url = '', data = {}) {
    return fetch(url, {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        redirect: 'follow',
        referrer: 'no-referrer',
        body: JSON.stringify(data),
    });
}

/*
moves an image right or left
*/
function moveCarouselImgRight(img) {
	return moveCarouselImg("RIGHT", img);
}

function moveCarouselImgLeft(img) {
	return moveCarouselImg("LEFT", img);
}

function moveCarouselImg(direction, img) {
	return fetch('/api/carouselImgs/move', {
		method: 'PUT',
		headers: {'Content-Type': 'application/json'},
		body: JSON.stringify({ direction: direction, image: img})
	});
}

/*
deletes an image
*/
function deleteData(url = '', data = {}) {
    return fetch(url, {
           method: 'DELETE',
           headers: {'Content-Type': 'application/json'},
           body: JSON.stringify(data)
       });
   }
   

export { getLatest,
     getPostById,
    getAbout,
    postData,
    getCarouselImages, 
    uploadFile,
    moveCarouselImgRight,
    moveCarouselImgLeft,
    deleteData
};

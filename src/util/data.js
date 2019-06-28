/*
For blogger API
*/
const key = "AIzaSyDyOzcQFTCjSmlT-nd5rbLXLca3glNkZhg";
// google id for testing: 3213900
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

export { getLatest, getPostById };
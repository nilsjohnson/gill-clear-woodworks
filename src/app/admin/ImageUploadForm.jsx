import React, { Component } from 'react';
import '../../css/app.scss';
import { getCarouselImages, uploadFile } from '../../util/data.js';
import Thumbnail from './Thumbnail.jsx';

class ImageUploadForm extends Component {
	constructor(props) {
		super(props);

		this.state = {
			thumbnails: []
		}

		let callback = this.addThumbnails;
    	getCarouselImages().then(function(response){
        if(response.ok) {
            response.json().then(data => { 
              callback(data);
          });
        }
        else {
          console.log("problem fetching carousel images");
        } 
      });
	}

	/*
	updates state to have an array of thumbnails
	*/
	addThumbnails = (addrs) => {
		for(let i = 0; i < addrs.length; i++) {
			this.addThumbnail(addrs[i]);
		}
	}
	
	/*
	adds an images to the state's array of thumbnails
	*/
	addThumbnail = (addr) => {
		this.setState({
			thumbnails: [...this.state.thumbnails, addr]
		});
	}

	/*
	removes a thumbnail from the state's array of thumbnails
	*/
	removeThumnail = (addr) => {

		let carouselImgs = this.state.thumbnails;
		let removed = false;

		for(let i = 0; i < carouselImgs.length; i++) {
			if(addr === carouselImgs[i]) {
				carouselImgs.splice(i, 1);
				removed = true;
			}
		}

		this.setState({
			thumbnails: carouselImgs
		});
	}

	/*
	uploads the content from the form to the server
	*/
	upload = () => {
		var photos = document.querySelector('input[type="file"][multiple]');

		// uploads each individual file
		for (var i = 0; i < photos.files.length; i++) {
			var formData = new FormData();
		  	formData.append('photo', photos.files[i]);

			uploadFile('/api/carouselUpload', formData)
			.then(response => {
				if(response.ok) {
					return response.json();
				}
				throw(response.status);
			})
			.then(resJson => this.addThumbnail(resJson.address))
			.catch(error => console.error('Error: ', error));
		}
		photos.value = "";
	}

	/*
	moves a thumbnail left or right
	*/
	shiftThumbnail = (direction, image) => {
		let carouselImgs = this.state.thumbnails;
		let imageIndex = 0;
		let found = false;

		while(imageIndex < carouselImgs.length) {
			if(carouselImgs[imageIndex] === image) {
				found = true;
				break;
			}
			else {
				imageIndex++;
			}
		}

		if(found) {

			let rightIndex, leftIndex, temp;

			if(imageIndex === 0) {
				leftIndex = carouselImgs.length -1;
				rightIndex = imageIndex + 1;
			}
			else if(imageIndex === carouselImgs.length - 1) {
				rightIndex = 0;
				leftIndex = imageIndex - 1;
			}
			else {
				rightIndex = imageIndex + 1;
				leftIndex = imageIndex -1;
			}
			

			temp = carouselImgs[imageIndex];

			if(direction === "RIGHT") {
				carouselImgs[imageIndex] = carouselImgs[rightIndex];
				carouselImgs[rightIndex] = temp;
			}
			if(direction === "LEFT") {
				carouselImgs[imageIndex] = carouselImgs[leftIndex];
				carouselImgs[leftIndex] = temp;
			}

			this.setState({
				thumbnails: carouselImgs
			});
			
		}
		else {
			console.log("Can't move image. Image not found");
		}
	}

	
	render() {
		return(
			 <div>
			 	<h3>Add Carousel Images:</h3>
			 	<div className="well"> 
				    <input type="file" multiple />
				    <button onClick={this.upload}>Upload</button>
	    		</div>
	    		<hr/>
	    		<h3>Current Carousel Images:</h3>
			 	<div className="row">
			 		{this.state.thumbnails.map(item => <Thumbnail
      					key={item}
      					addr={item}
      					remove={this.removeThumnail}
      					shift={this.shiftThumbnail}/>)
      				}
			 	</div>
    		</div>     
            ); 
	}
}

export default ImageUploadForm;    
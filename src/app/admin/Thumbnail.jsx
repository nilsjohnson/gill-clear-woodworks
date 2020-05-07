import React, { Component } from 'react';
import '../../css/app.scss';
import { deleteData, moveCarouselImgLeft, moveCarouselImgRight } from '../../util/data.js';

class Thumbnail extends Component {
    
    /*
    API call to delete image
    */
    delete = () => {
        deleteData('/api/carouselImgDelete', { image: this.props.addr })
            .then(response => this.removeThumbnail(response))
            .catch(err => console.log(err));
    }

    /*
    removes image from view if successfully deleted
    */
    removeThumbnail = (response) => {
        if(response.ok){
            this.props.remove(this.props.addr);
        }
        else {
            console.log("problem deleting image.")
            console.log(response);
        }
    }

    moveThumbnailLeft = () => {
        let callback = this.props.shift;
        let addr = this.props.addr;
        moveCarouselImgLeft(this.props.addr)
        .then(function(response){
            if(response.ok) {
                callback("LEFT", addr);
            }
            else {
                console.log("problem shifting image left");
            }
        });
    }

    moveThumbnailRight = () => {
        let callback = this.props.shift;
        let addr = this.props.addr;
        moveCarouselImgRight(this.props.addr)
        .then(function(response){
            if(response.ok) {
                callback("RIGHT", addr);
            }
            else {
                console.log("problem shifting image right");
            }
        });
    }

	render() {
		return(
			<div className="col-lg-3 col-md-4 col-sm-6 well">
    			<img className="img-fluid img-thumbnail" src={this.props.addr}/>
    			<button type="button" onClick={this.delete} className="btn btn-primary btn-lg btn-block">Delete</button>
    			<button type="button" onClick={this.moveThumbnailLeft} className="btn btn-secondary btn-lg btn-block">Left</button>
    			<button type="button" onClick={this.moveThumbnailRight} className="btn btn-secondary btn-lg btn-block">Right</button>
    		</div>     
            ); 
	}
}

export default Thumbnail;   
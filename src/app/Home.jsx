import React, { Component } from 'react';
import Navbar from '../component/Navbar.jsx';
import CarouselWrapper from '../component/CarouselWrapper.jsx';
import SocialBar from '../component/SocialBar.jsx';
import Footer from '../component/Footer.jsx';
import { getAbout, getCarouselImages } from '../util/data.js';

class Home extends Component {
	constructor(props) {
		super(props);

		this.state = {
			about: "", // null here?
			carouselImages: []
		};

		// TODO .catch
		let callback = this.setAbout;
		getAbout().then(function(response) {
			if(response.ok) {
				response.json().then(data => {
					callback(data.about);
				});
			}
			else {
				console.log("problem getting about");
			}
		});

		let callback_2 = this.setImages;
		getCarouselImages().then(function(response) {
			if(response.ok) {
				response.json().then(data => {
					callback_2(data);
				});
			}
			else {
				console.log("problem getting about");
			}
		}).catch(err => {
			console.log("problem fetching carousel images.");
			console.log(err)
		});




	}

	setImages = (images) => {
		console.log(images);
		this.setState({carouselImages: images});
	}

	setAbout = (about) => {
		console.log(about);
		this.setState({about: about});
	}

	render() {
		return (
			<div className="container">
				<Navbar
					activePage={"home"} 
				/>
				<div className="row">
					<div className="col-xl-8">
						<CarouselWrapper
							images={this.state.carouselImages}
						/>
					</div>
					<div className="col-xl-4">
						<h4 className="text-center">About</h4>
						<p>
							{ this.state.about }
						</p>
						<h4 className="text-center">Contact</h4>
			            <div className="row">
			                <div className="col-6">
			                	<div>
			                    	267 Main Rd.
			                	</div>
			                	<div>
			                   		Gill, MA 01354
			                	</div>
			                </div>
			                <div className="col-6">
			                	<div>Sam French</div>
			                    <a href="mailto:gillccwoodworks@gmail.com">gillccwoodworks@gmail.com</a>
			                    <div>(413) 627-7020</div>
			                </div>
			            </div>   
					</div>
				</div>
				<hr/>
				<SocialBar/>
				<Footer/>
			</div>
		);
	}
}

export default Home;


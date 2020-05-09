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
			about: "",
			addr_1: "",
			add_2: "",
			email: "",
			carouselImages: []
		};

		getAbout().then(response => {
			if(response.ok) {
				console.log(response);
				return response.json();
			}
			throw(response.status);
		})
		.then(resJson => this.setFields(resJson))
		.catch( err => console.log(`Problem getting 'about': ${err}`));

		getCarouselImages().then(response => {
			if(response.ok) {
				return response.json();
			}
		}).then(resJson => {
			this.setImages(resJson);
		}).catch(err => console.log(`error: ${err}`));
	}

	setImages = (images) => {
		console.log(images);
		this.setState({carouselImages: images});
	}

	setFields = (object) => {
		console.log(object);
		const addr = new Array(2);
		const temp = object.addr.split('\n');
		if(addr.length >= 2) {
			addr[0] = temp[0];
			addr[1] = temp[1];
		}
		else if(addr.length = 1) {
			addr[0] = temp[1];
		}

		this.setState({
			about: object.about,
			addr_1: addr[0],
			addr_2: addr[1],
			phone: object.phone,
			message: object.message,
			email: object.email
		});
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
			                    	{this.state.addr_1}
			                	</div>
			                	<div>
									{this.state.addr_2}
			                	</div>
			                </div>
			                <div className="col-6">
			                	<div>Sam French</div>
		<a href="mailto:gillccwoodworks@gmail.com">{this.state.email}</a>
			                    <div>{ this.state.phone }</div>
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


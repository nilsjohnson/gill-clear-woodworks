import React, { Component } from 'react';
import Navbar from '../component/Navbar.jsx';
import CarouselWrapper from '../component/CarouselWrapper.jsx';
import SocialBar from '../component/SocialBar.jsx';
import Footer from '../component/Footer.jsx';
import { getAbout } from '../util/data.js';

class Home extends Component {
	constructor(props) {
		super(props);

		this.state = {
			about: ""
		};

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
							images={[
								"/img/eleven.jpg",
								"/img/twelve.jpg",
								"/img/one.jpg",
								"/img/two.jpg",
								"/img/three.jpg",
								"/img/four.jpg",
								"/img/five.jpg",
								"/img/six.jpg",
								"/img/seven.jpg",
								"/img/eight.jpg",
								"/img/ten.jpg",
								"/img/thirteen.jpg",
								"/img/fourteen.jpg"]}
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


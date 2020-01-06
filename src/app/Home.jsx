import React, { Component } from 'react';
import Navbar from '../component/Navbar.jsx';
import CarouselWrapper from '../component/CarouselWrapper.jsx';
import SocialBar from '../component/SocialBar.jsx';
import Footer from '../component/Footer.jsx';
import BlogSelector from '../component/blog/BlogSelector.jsx';

class Home extends Component {
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
								"https://s3.amazonaws.com/forestfriends/November2017-53.jpg", 
								"https://s3.amazonaws.com/forestfriends/TablesJanuary-57.jpg",
								"https://s3.amazonaws.com/forestfriends/IMG_0102.JPG",
								"https://s3.amazonaws.com/forestfriends/TablesJanuary-66.jpg",
								"https://s3.amazonaws.com/forestfriends/bed frame_505.JPG",
								"https://s3.amazonaws.com/forestfriends/birch table_656.JPG",
								"https://s3.amazonaws.com/forestfriends/boston table_797.JPG",
								"https://s3.amazonaws.com/forestfriends/trestle desk_408.JPG",
								"https://s3.amazonaws.com/forestfriends/trestle desk_400.JPG",
								"https://s3.amazonaws.com/forestfriends/Leftys-62.jpg",
								"https://s3.amazonaws.com/forestfriends/daybed_762.JPG",
								"https://s3.amazonaws.com/forestfriends/dining table.jpg",
								"https://s3.amazonaws.com/forestfriends/maple_table_one.jpg"]}
						/>
					</div>
					<div className="col-xl-4">
						<h4 className="text-center">About</h4>
						<p>
							Gill Country Clear Wood Works specializes in a wide variety of fine custom furniture. From full bars to coffee tables we strive to harness the natural beauty of the wood and give each piece its own distinct voice.  We sustainably harvest and mill the majority of our lumber right on site at the shop. We carry a large inventory of hardwood slabs and other dimensional lumber. From tree to table custom made for your home or business.
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


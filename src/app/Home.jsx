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
				<CarouselWrapper
					images={["https://s3.amazonaws.com/forestfriends/TablesJanuary-57.jpg", "https://s3.amazonaws.com/forestfriends/IMG_0102.JPG"]}
				/>
				<SocialBar/>
				<Footer/>
			</div>
		);
	}
}

export default Home;
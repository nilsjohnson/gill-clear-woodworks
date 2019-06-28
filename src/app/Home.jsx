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
						"https://s3.amazonaws.com/forestfriends/maple_table_one.jpg"

				]}
				/>
				<SocialBar/>
				<Footer/>
			</div>
		);
	}
}

export default Home;
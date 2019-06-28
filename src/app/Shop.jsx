import React, { Component } from 'react';
import Navbar from '../component/Navbar.jsx';
import Footer from '../component/Footer.jsx';
import BlogSelector from '../component/blog/BlogSelector.jsx';
import PostContainer from '../component/blog/PostContainer.jsx';

class Shop extends Component {
	render() {
		let url = new URL(window.location.href);
		let query = url.searchParams.get("post");

		return (
			<div className="container">
				<Navbar
					activePage={"blog"}
				/>
				<div>
					{query ? 
						<PostContainer postId={query}/> : <BlogSelector/>}
				</div>
				<Footer/>
			</div>
		);
	}
}

export default Shop;
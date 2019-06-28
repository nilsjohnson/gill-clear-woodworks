import React, { Component } from 'react';
import '../../css/app.scss';
import { getLatest } from '../../util/data.js';
import BlogHeader from './BlogHeader.jsx';

class BlogSelector extends Component {
	constructor(props) {
		super(props);

		this.state = {
			posts: []
		};

		let callback = this.setPosts;
		// fetches the latests blog posts
		getLatest(this.props.maxResults).then(function(response) {
		if(response.ok) {
			response.json().then(data => {
				callback(data.items);
			});
		}
		else {
			console.log("problem fetching latest posts")
		}
	});
}

	setPosts = (posts) => {
		console.log(posts);
		this.setState({posts: posts});
		
	}
	
 	render() {
    	return (
      	<div>
      		{this.state.posts.map(item => <BlogHeader
      			key={item.id}
				post={item}/>)
      		}
     	</div>
    	);
  }
}

export default BlogSelector;


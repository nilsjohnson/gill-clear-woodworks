import React, { Component } from 'react';
import '../../css/app.scss';
import { getLatest, getPostById } from '../../util/data.js';
import BlogHeader from './BlogHeader.jsx';

class PostContainer extends Component {
	constructor(props) {
		super(props);

		this.state = {
			content: "",
			title: "",
			date: ""
		};

		let callback = this.setPost;

		getPostById(this.props.postId).then(function(response) {
			if(response.ok) {
				response.json().then(data => {
					//console.log(data);
					callback(data);
					
				});
			}
			else {
				console.log("problem fetching latest posts")
			}
		});
	}

	setPost = (post) => {
		this.setState({
			content: post.content, 
			title: post.title
			});
	}

 	render() {
    	return (
      	<div>
   			<div id="post-container">
   				<h2 className="text-center">
   					{this.state.title}
   				</h2>
   				<hr/>
      		</div>
      		<div className="trans container" id="post-container" 
      			dangerouslySetInnerHTML={{__html: this.state.content}}>
      		</div>
     	</div>
    	);
  }
}

export default PostContainer;

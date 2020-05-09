import React, { Component } from 'react';
import '../../css/app.scss';
import { Link } from 'react-router-dom';
import NoImage from '../../img/noImage.png';



class BlogHeader extends Component {
  getImage = () => {
    if (typeof this.props.post.images === "undefined") {
      return NoImage;
    }
    else if (this.props.post.images[0].url) {
      return this.props.post.images[0].url;
    }
    else {
      return NoImage;
    }
  }
  render() {
    return (
      <div className="col-lg-4 col-md-6 col-sm-12 box">
    		{/* The Image */}
        <Link to={{
           pathname: '/portfolio',
           search: '?post=' + this.props.post.id
          }}>
            <img src={this.getImage()} className="img-fluid thumbnail rounded mx-auto d-block"/>
        </Link>
        {/* The text */}
        <Link to={{
           pathname: '/portfolio',
           search: '?post=' + this.props.post.id
          }}>
          <h5 className="text-center">{this.props.post.title}</h5>
        </Link>
    	</div>
    );
  }
}

export default BlogHeader;
import React, { Component } from 'react';
import '../../css/app.scss';
import { Link } from 'react-router-dom';
import NoImage from '../../img/noImage.png';
import { toReadable } from '../../util/util';



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
      <div className="box">
        <div className="row">
          <div className="col-md-4">
            <Link to={{
                   pathname: '/woodshop',
                   search: '?post=' + this.props.post.id
                }}><img src={this.getImage()} className="img-fluid thumbnail"/></Link>
          </div>
          <div className="col-md-8">
          <Link to={{
                   pathname: '/woodshop',
                   search: '?post=' + this.props.post.id
                }}><h3 className="text-center">{this.props.post.title}</h3></Link>
            <h4>{toReadable(this.props.post.published)}</h4>
          </div>
        </div>
        <hr/>
      </div>
    );
  }
}

export default BlogHeader;

import React, { Component } from 'react';
import ImageUploadForm from './ImageUploadForm.jsx';

class AdminLanding extends Component {
    constructor(props) {
        super(props);

        this.state = {
            about: "",
            message: ""
        };

    }

    updateMessage = (event) => {
        this.setState({message: event.target.value});
    }

    render() {
        return (
        <div>

            <ImageUploadForm />

			<div className="form-group row">
	  			<label className="col-sm-3 col-form-label">About</label>
	  			<div className="col-sm-9">
	   				<textarea
	   				 	type="text" className="form-control" id="event-name"
	   				 	onChange={this.updateMessage}
	   				/>
	  			</div>
			</div>
        </div>
        );
    }

}

export default AdminLanding;
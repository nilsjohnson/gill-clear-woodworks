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
        </div>
        );
    }

}

export default AdminLanding;
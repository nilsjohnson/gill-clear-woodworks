import React, { Component } from 'react';
import ImageUploadForm from './ImageUploadForm.jsx';
import { getAbout, postData } from '../../util/data.js';

class AdminLanding extends Component {
    constructor(props) {
        super(props);

        this.state = {
            about: "",
            phone: "",
            addr: "",
            email: "",
            message: ""
        };

        getAbout().then(response => {
			if(response.ok) {
				return response.json();
			}
			throw(response.status);
		})
		.then(resJson => this.setFields(resJson))
		.catch( err => console.log(`Problem getting 'about': ${err}`));

    }

    // sets all the state fields.
    setFields = (object) => {
        this.setState(object);
    }

    setAbout = (event) => {
        this.setState({about: event.target.value});
    }

    setEmail = (event) => {
        this.setState({email: event.target.value});
    }

    setPhone = (event) => {
        this.setState({phone: event.target.value});
    }

    setMessage = (event) => {
        this.setState({ message: event.target.value });
    }

    setAddr = (event) => {
        this.setState({ addr: event.target.value });
    }

    updateData = () => {
        postData('api/updateAbout', this.state).then(response => {
            if(response.ok) {
                alert("update success!");
            }
            else {
                alert("Problem updating.");
            }
        }).catch(err => console.log(`error: ${err}`));
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-sm-6">
                        <div className="form-group">
                            <label>About</label>
                            <textarea className="form-control" onChange={this.setAbout} value={this.state.about}/>
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <label>Email</label>
                        <input className="form-control" onChange={this.setEmail} value={this.state.email}/>

                        <label>Phone</label>
                        <input className="form-control" onChange={this.setPhone} value={this.state.phone}/>

                        <label>Address</label>
                        <textarea className="form-control" onChange={this.setAddr} value={this.state.addr}/>

                        <label>Message</label>
                        <input className="form-control" onChange={this.setMessage} value={this.state.message}/>
                    </div>


                    <button onClick={this.updateData}>Save</button>
                </div>



                <ImageUploadForm />
            </div>
        );
    }

}

export default AdminLanding;
import React, { Component } from 'react';
import Navbar from '../component/Navbar.jsx';
import Footer from '../component/Footer.jsx';
import PwModal from './admin/PwModal.jsx';
import AdminLanding from './admin/AdminLanding.jsx';
import { setCookie, getCookie } from '../util/util.js';

class Admin extends Component {
   constructor(props) {
       super(props);

        let authToken = getCookie("auth_token");

       this.state = {
        authToken: authToken
       };

    }

    setAuthToken = (authToken) => {
        if(authToken != null) {
            console.log("setting auth token...");
            console.log(authToken);
            setCookie("auth_token", authToken, 600);
            this.setState({authToken: authToken});
        }
        else {
            console.log("auth token was null");
        }
        
        
    }


    render() {
        if(this.state.authToken != null) {
            return (
                <div className="container">
                <Navbar/>
                <AdminLanding />
				<Footer/>
			</div>
            );
        }
        else {
            return (
                <div className="container">
                    <PwModal
                        setAuthToken={this.setAuthToken}
                    />
                    <Footer/>
                </div>
            );
        }
		
	}
}

export default Admin;
import React, { Component } from 'react';
import Navbar from '../component/Navbar.jsx';
import Footer from '../component/Footer.jsx';
import PwModal from './admin/PwModal.jsx';
import AdminLanding from './admin/AdminLanding.jsx';
import { setCookie, getCookie, deleteCookie } from '../util/util.js';
import '../css/app.scss';

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
            setCookie("auth_token", authToken, 20*60);
            this.setState({authToken: authToken});
        }
        else {
            console.log("auth token was null");
        }
    }

    logout = () => {
        deleteCookie("auth_token");
        this.redirectHome();
    }

    redirectHome = () => {
        this.props.history.push('/');
    }


    render() {
        if(this.state.authToken != null) {
            return (
                <div className="container">
                <Navbar/>
                <button className="btn btn-secondary float-right" onClick={this.logout}> Logout</button>
                <h2 className="text-center">Admin Page</h2>
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
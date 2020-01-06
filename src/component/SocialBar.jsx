import React, { Component } from 'react';
import '../css/app.scss';
import Instagram from '../img/instagram.png';
import Facebook from '../img/facebook.png';

class SocialBar extends Component {
    render() {
        return (
            <div className="row">
                <div className="mx-auto d-block">

                    <a href="https://www.instagram.com/gill_cc_woodworks/" className="social-link">
                        <img src={Instagram} />
                    </a>
                    <a href="https://www.facebook.com/gillccwoodworks/" className="social-link">
                        <img src={Facebook} />
                    </a>
                </div>
            </div>
        );
    }
}

export default SocialBar;
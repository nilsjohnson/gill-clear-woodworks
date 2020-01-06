import React, { Component } from 'react';
import '../css/app.scss';

class Navbar extends Component {
    render() {
        return (
            <div className="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom shadow-sm">
                <h5 className="my-0 mr-md-auto font-weight-normal">Gill Country Clear Woodworks</h5>
                <nav className="my-2 my-md-0 mr-md-3">
                    <a className={this.props.activePage === "home" ? "p-2 text-dark active" : "p-2 text-dark"} href="/">Home</a>
                    <a className={this.props.activePage === "blog" ? "p-2 text-dark active" : "p-2 text-dark"} href="/woodshop">Portfolio</a>
                   {/*  <a className={this.props.activePage === "gallery" ? "p-2 text-dark active" : "p-2 text-dark"} href="gallery">Gallery</a>
                    <a className={this.props.activePage === "about" ? "p-2 text-dark active" : "p-2 text-dark"} href="about">About</a> */}
                </nav>
            </div>
        );
    }
}

export default Navbar;
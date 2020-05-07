import React from 'react';
import ReactDOM from 'react-dom';
import Home from './app/Home.jsx';
import Shop from './app/Shop.jsx';
import { BrowserRouter, Route } from 'react-router-dom';
import Admin from './app/Admin.jsx';

let elem = document.getElementById('root');

let router = (
	<BrowserRouter>
		<div>
			<Route path="/" component={Home} exact/>
			<Route path="/woodshop" component={Shop}/>
			<Route path="/admin" component={Admin}/>
		</div>
	</BrowserRouter>
	);

ReactDOM.render(router, elem);




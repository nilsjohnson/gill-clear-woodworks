import React from 'react';
import ReactDOM from 'react-dom';
import Home from './app/Home.jsx';
import { BrowserRouter, Route } from 'react-router-dom';

let elem = document.getElementById('root');

let router = (
	<BrowserRo uter>
		<div>
			<Route path="/" component={Home} exact/>
		</div>
	</BrowserRouter>
	);

ReactDOM.render(router, elem);




/*
If running in production on server, pass 'prudction' as an argument:
	$ node server.js production
This will allow listening for https requests, otherwise it will just do http.	
*/
const PRODUCTION = 1;
const DEV = 0;
let mode;

if (process.argv.length > 2 && process.argv[2] === "production") {
	console.log("Server running in prodcution. Will listen for https requests.");
	mode = PRODUCTION;
}
else {
	mode = DEV;
	console.log("Server running in DEV mode. Will not listen for https requests");
}

const express = require('express');
const path = require('path');
const app = express();
exports.app = app;
const bodyParser = require('body-parser');
const fs = require('fs');
const https = require('https');

const imgApi = require('./imageApi.js');
const authAip = require('./authApi');

const HTTP_PORT_NUM = 3000;
const HTTPS_PORT_NUM = 443;

// for https
let FULL_CHAIN;
let PRIVATE_KEY;
let OPTIONS = {};

if (mode == PRODUCTION) {
	FULL_CHAIN = '/etc/letsencrypt/live/www.gillccwoodworks.com/fullchain.pem';
	PRIVATE_KEY = '/etc/letsencrypt/live/www.gillccwoodworks.com/privkey.pem';
	OPTIONS = {
		cert: fs.readFileSync(FULL_CHAIN),
		key: fs.readFileSync(PRIVATE_KEY)
	};
}

app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, '../build'), { index: false })); // index : false is to allow request for the webroot to get caught by 'app.get('/*', function(req, res)', allowing http to https redirects
app.use(express.static(path.join(__dirname, '../public'), { index: false }));

app.get('/about', function (req, res) {
	console.log("about hit!");
	res.json({about: 'Gill Country Clear Wood Works specializes in a wide variety of fine custom furniture. From full bars to coffee tables we strive to harness the natural beauty of the wood and give each piece its own distinct voice.  We sustainably harvest and mill the majority of our lumber right on site at the shop. We carry a large inventory of hardwood slabs and other dimensional lumber. From tree to table custom made for your home or business.'});
});

app.get('/*', function (req, res) {
	if (mode === PRODUCTION) {
		let secure = req.secure;
		let hasSubDomain = req.headers.host.startsWith("www");

		if(secure && hasSubDomain) {
			// good
			res.sendFile(path.join(__dirname, '../public', 'index.html'));
		}
		else if(secure && !hasSubDomain) {
			// needs to be redirected to subdomain
			res.redirect("https://www." + req.headers.host + req.url);
		}
		else if(!secure && hasSubDomain) {
			// needs to be redirected securely to subdomain
			res.redirect("https://" + req.headers.host + req.url);
		}
		else if(!secure && !hasSubDomain) {
			// needs to be redirected to https
			res.redirect("https://www" + req.headers.host + req.url);
		}
	}
	else {
		res.sendFile(path.join(__dirname, '../public', 'index.html'));
	}

});




/*
Starts server
*/
app.listen(HTTP_PORT_NUM, function () {
	console.log('Listening on port 3000 for requests');
});

/*
Starts listening for https requests
*/
if (mode == PRODUCTION) {
	https.createServer(OPTIONS, app).listen(HTTPS_PORT_NUM);
}
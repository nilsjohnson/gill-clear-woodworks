/*
If running in production on server, pass 'prudction' as an argument:
	$ node server.js production
This will allow listening for https requests, otherwise it will just do http.	
*/
const PRODUCTION = "A unique String! :)";
let mode;

if (process.argv.length > 2 && process.argv[2] === "production") {
	console.log("Server running in prodcution. Will listen for https requests.");
	mode = PRODUCTION;
}
else {
	console.log("Server running in DEV mode. Will not listen for https requests");
}

const express = require('express');
const path = require('path');
const app = express();
const fs = require('fs');
const https = require('https');

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

app.use(express.static(path.join(__dirname, '../build'), { index: false })); // index : false is to allow request for the webroot to get caught by 'app.get('/*', function(req, res)', allowing http to https redirects
app.use(express.static(path.join(__dirname, '../public'), { index: false }));



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
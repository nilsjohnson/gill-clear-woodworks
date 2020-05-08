const { app } = require("./server");
const crypto = require('crypto');
const bodyParser = require('body-parser');
const fileUtil = require('./fileUtil.js');
app.use(bodyParser.json());

const TOKENS_FILENAME = "auth_tokens.json";
let tokens;

/**
 * loads tokens from disk
 */
function loadTokens() {
	tokens = fileUtil.readObj(TOKENS_FILENAME);
	if(tokens === null) {
		tokens = {};
	}
}

/**
 * Deletes tokens more than 1 day old.
 */
function deleteExpiredTokens() {
	let d = new Date();
	d.setDate(d.getDate() - 1);

	for(const token in tokens) {
		if (tokens[token] < d ) {
			console.log(`Token Expired. Deleting token: ${token}`);
			deleteToken(token);
		}
	}
}

/**
 * @param {Object} headers 
 */
function extractToken(headers) {
	return headers.authorization.replace('Token ', '');
}

/**
 * Writes tokens to disk.
 */
function writeTokens() {
	deleteExpiredTokens();
	fileUtil.writeObj(tokens, TOKENS_FILENAME);
}

/**
 * 
 * @param {string} token Token to delete. 
 */
function deleteToken(token) {
	delete tokens[token];
}

/**
 * Generates a 128 bit random authentication token and date.
 */
function generateToken() {
	const buffer = crypto.randomBytes(16);
	const token = buffer.toString('hex');
	tokens[token] = new Date();
	writeTokens();
	return token;
}

/**
 * @param {Object} header The request header
 * @param {*} res The response for sending failure.
 * 
 * Resets the authentication token date to now, 
 * or sends 403 response code for invalid tokens.
 */
function authenticate(header, res) {
	let token = extractToken(header);
	if(token in tokens) {
		tokens[token] = new Date();
		return true;
	}
	res.status(403);
	res.send();
	console.log("Not an authenticated request.");
	return false;
}

/**
 * Api endpoint for getting authentication token. 
 */
app.post('/api/authenticate', function (req, res) {
	console.log("auth");
	if (req.body.password === "maple") {
		let token = generateToken();
		console.log("good login attempt");
		res.json({ authToken: token });
		res.status(200);
	}
	else {
		console.log("bad login attempt");
		res.status(401);
	}
	res.send();
});

loadTokens();
deleteExpiredTokens();


exports.authenticate = authenticate;
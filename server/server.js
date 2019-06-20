const express = require('express');
const path = require('path');
const app = express();

app.use(express.static(path.join(__dirname, '../build'), { index : false })); // index : false is to allow request for the webroot to get caught by 'app.get('/*', function(req, res)', allowing http to https redirects
app.use(express.static(path.join(__dirname, '../public'), { index : false })); 



app.get('/*', function(req, res) {
  	res.sendFile(path.join(__dirname, '../public', 'index.html'));
});


app.listen(3000, function() {
  console.log('Example app listening on port 3000!');
});
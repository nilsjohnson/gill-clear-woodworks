const { app } = require("./server");
const fileUtil = require("./fileUtil.js");
const bodyParser = require('body-parser');
const auth = require('./authApi.js');


const CONTENT_FILENAME = "content.json"
let content = null;

/**
 * reads the content from disk, or sets to default values.
 */
function loadContent() {
    content = fileUtil.readObj(CONTENT_FILENAME);
    if(content === null) {
        content = {
            about: "",
            phone: "",
            addr: "",
            email: ""
        };
    }
}

/**
 * updates the content values
 */
function updateContent(object) {
    content = object;
    fileUtil.writeObj(object, CONTENT_FILENAME);
}


/**
 * To update the about section
 */

 app.post('/api/updateAbout', function(req, res) {
    if(!auth.authenticate(req.headers, res)) { return; }
    updateContent(req.body);
    res.status(200);
    res.send();
 });

/**
 * To get the public about section
 */
app.get('/about', function (req, res) {
    res.json(content);
});

loadContent();
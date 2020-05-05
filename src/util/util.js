function toReadable(date_str) {
	const date = new Date(date_str);
	return (
		date.getMonth() + 1) + 
		'/' + date.getDate() + '/' +
		 date.getFullYear();//', ' + date.getHours() + ':' + (date.getMinutes()<10?'0':'') + date.getMinutes();
}

function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i = 0; i < ca.length; i++) {
	  var c = ca[i];
	  while (c.charAt(0) == ' ') {
		c = c.substring(1);
	  }
	  if (c.indexOf(name) == 0) {
		  return c.substring(name.length, c.length);
	  }
	}
	return null;
  }
  
  
  function setCookie(variable, value, expires_seconds) {
	  var d = new Date();
	  d = new Date(d.getTime() + 1000 * expires_seconds);
	  document.cookie = variable + '=' + value + '; expires=' + d.toGMTString() + ';';
  }
  
  export { 
	toReadable,
	getCookie,
	setCookie,
  };
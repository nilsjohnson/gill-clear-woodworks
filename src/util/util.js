function toReadable(date_str) {
	const date = new Date(date_str);
	return (
		date.getMonth() + 1) + 
		'/' + date.getDate() + '/' +
		 date.getFullYear();//', ' + date.getHours() + ':' + (date.getMinutes()<10?'0':'') + date.getMinutes();
}

export {
    toReadable
};
function Security() {
};

Security.prototype.init = function() {
	// placeholder
};

Security.prototype.getToken = function() {
	return 'Bearer ' + auth_token;
};

Security.prototype.checkLogin = function() {
	var t = this.getToken();
	if (t != null) {
		return true;
	} else {
		this.logout();
	}
};

Security.prototype.logout = function() {
	document.location = __Properties.logoutUrl;
};

Security.prototype.error = function(jqXHR, textStatus, errorThrown) {
	alert("Error: " + textStatus);
};
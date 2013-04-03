function Security() {
};

Security.prototype.init = function() {
	// placeholder
};

Security.prototype.clearToken = function() {
	var i, x, ARRcookies = document.cookie.split(';');
	for (i = 0; i < ARRcookies.length; i++) {
		x = ARRcookies[i].substr(0, ARRcookies[i].indexOf('='));
		x = x.replace(/^\s+|\s+$/g, '');
		if (x == 'auth_token') {
			document.cookie = x + '=; expires=Thu, 01-Jan-70 00:00:01 GMT';
		}
	}
};

Security.prototype.getToken = function() {
	var i, x, y, ARRcookies = document.cookie.split(';');
	for (i = 0; i < ARRcookies.length; i++) {
		x = ARRcookies[i].substr(0, ARRcookies[i].indexOf('='));
		y = ARRcookies[i].substr(ARRcookies[i].indexOf('=') + 1);
		x = x.replace(/^\s+|\s+$/g, '');
		if (x == 'auth_token') {
			return unescape(y);
		}
	}
};

Security.prototype.checkLogin = function() {
	var t = this.getToken();
	if (t != null) {
		return true;
	} else if (document.location.hash != null && document.location.hash.length > 1) {
		var code = document.location.hash.substr(1);
		$.ajax({
			dataType: "xml",
			sync : false,
			type : 'POST',
			url : __Properties.validationUrl+code,
			success : function(data, textStatus, jqXHR) {
				var token = $(data).find("token").text();
				document.cookie = 'auth_token=' + token;
		 		document.location = __Properties.baseUrl;
			},
			error : function(jqXHR, textStatus, errorThrown) {
				__Sec.authenticate();
			}
		});
	} else {
		this.authenticate();
	}
};

Security.prototype.authenticate = function() {
	document.location = __Properties.authUrl + __Properties.baseUrl;
};

Security.prototype.logout = function() {
	this.clearToken();
	document.location = __Properties.baseUrl;
};

Security.prototype.error = function(jqXHR, textStatus, errorThrown) {
	alert("Error: " + textStatus);
};
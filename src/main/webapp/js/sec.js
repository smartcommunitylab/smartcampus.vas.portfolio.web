/**
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
			sync : false,
			type : 'POST',
			url : __Properties.validationUrl+code,
			success : function(data, textStatus, jqXHR) {
				var token = data.substr(1,data.length-2);
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

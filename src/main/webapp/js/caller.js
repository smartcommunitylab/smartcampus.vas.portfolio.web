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
function Caller() {
};

Caller.prototype.load = function(snippet, containerSelector, callback) {
	$(containerSelector).empty();
	$.get(__Properties.baseUrl + 'html/' + snippet + '.html', function(data) {
		$(containerSelector).html(data);
		if (callback != null) {
			callback();
		}
	});
};

Caller.prototype.createStudent = function() {
	$.ajax({
		async : false,
		type : 'POST',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/createstudent',
		success : function(data, textStatus, jqXHR) {
			__IsStudent = JSON.parse(data);
			__Main.go('manager');
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

Caller.prototype.getOverview = function() {
	$.ajax({
		type : 'GET',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.StudentInfo',
		beforeSend : function() {
			$('#overview').append($('<div></div>').addClass('icon loader'));
		},
		success : function(data, textStatus, jqXHR) {
			var renderer = new Renderer_Overview();
			renderer.render(data);
			__Utils.refreshUI();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			// alert(textStatus + ' ' + errorThrown);
		}
	});
};

Caller.prototype.getOverviewUserContent = function(category, containerSelector) {
	$.ajax({
		type : 'GET',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.UserProducedData/' + category,
		success : function(data, textStatus, jqXHR) {
			// cherries
			if (__Current == 'myportfolios' && __EditMode == false) {
				var parsedData = JSON.parse(data);
				var myPortfoliosCurrentContent = JSON.parse(__MyPortfoliosCurrent.content);
				var hugd = myPortfoliosCurrentContent['highlightUserGeneratedData'];
				var cherries = __Utils.getEntriesByIds(parsedData, hugd);
				for ( var p = 0; p < cherries.length; p++) {
					__MyPortfoliosCurrentCherries.push(cherries[p]);
				}

				if (__MyPortfoliosCurrentCherries.length > 0 && __MyPortfoliosCurrentCherriesDone == false) {
					// == hugd.length
					// __MyPortfoliosCurrentCherriesDone = true;
					var rendererSection = new Renderer_Section();
					rendererSection.render('cherryotc', null);
				}
			}

			var renderer = new Renderer_OverviewSection();
			renderer.render(category, containerSelector, data);
			__Utils.refreshUI();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			// alert(textStatus + ' ' + errorThrown);
		}
	});
};

Caller.prototype.getUserContent = function(category) {
	var container = $(__Properties.categories[category].containerSelector);

	$.ajax({
		type : 'GET',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.UserProducedData/' + category,
		beforeSend : function() {
			$(container).append($('<div></div>').addClass('icon loader'));
		},
		success : function(data, textStatus, jqXHR) {
			// cherries
			if (__Current == 'myportfolios' && __EditMode == false) {
				var parsedData = JSON.parse(data);
				var myPortfoliosCurrentContent = JSON.parse(__MyPortfoliosCurrent.content);
				var hugd = myPortfoliosCurrentContent['highlightUserGeneratedData'];
				var cherries = __Utils.getEntriesByIds(parsedData, hugd);
				for ( var p = 0; p < cherries.length; p++) {
					__MyPortfoliosCurrentCherries.push(cherries[p]);
				}

				if (__MyPortfoliosCurrentCherries.length > 0 && __MyPortfoliosCurrentCherriesDone == false) {
					// == hugd.length
					// __MyPortfoliosCurrentCherriesDone = true;
					var rendererSection = new Renderer_Section();
					rendererSection.render('cherryotc', null);
				}
			}

			var renderer = new Renderer_Section();
			renderer.render(category, data);
			__Utils.refreshUI();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			// alert(textStatus + ' ' + errorThrown);
		}
	});
};

/* EDIT */
Caller.prototype.createUserContent = function(category, data) {
	/*
	 * category, type, title, subtitle, content
	 */
	var json = JSON.stringify(data);

	$.ajax({
		async : false,
		type : 'POST',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		contentType : 'application/json',
		data : json,
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.UserProducedData',
		success : function(data, textStatus, jqXHR) {
			__Caller.refresh([ category ]);
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

Caller.prototype.deleteUserContent = function(category, id) {
	$.ajax({
		async : false,
		type : 'DELETE',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.UserProducedData/' + id,
		success : function(data, textStatus, jqXHR) {
			__Caller.refresh([ category ]);
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

Caller.prototype.updateUserContent = function(category, id, data) {
	/*
	 * category, type, title, subtitle, content
	 */
	var json = JSON.stringify(data);

	$.ajax({
		async : false,
		type : 'PUT',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		contentType : 'application/json',
		data : json,
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.UserProducedData/' + id,
		success : function(data, textStatus, jqXHR) {
			__Caller.refresh([ category ]);
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

/*
 * My Portfolios
 */
Caller.prototype.getPortfolios = function(tabMenuSelector) {
	$.ajax({
		async : false,
		type : 'GET',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.Portfolio',
		beforeSend : function() {
			// $('#overview').append($('<div></div>').addClass('icon loader'));
		},
		success : function(data, textStatus, jqXHR) {
			__MyPortfolios = JSON.parse(data);
			__Renderer_TabMenu.render(tabMenuSelector, data);
			__Utils.refreshUI();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			// alert(textStatus + ' ' + errorThrown);
		}
	});
};

Caller.prototype.createPortfolio = function(data) {
	var json = JSON.stringify(data);

	$.ajax({
		async : false,
		type : 'POST',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		contentType : 'application/json',
		data : json,
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.Portfolio',
		beforeSend : function() {
			// $('#overview').append($('<div></div>').addClass('icon loader'));
		},
		success : function(data, textStatus, jqXHR) {
			__Main.go('myportfolios');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			// alert(textStatus + ' ' + errorThrown);
		}
	});
};

Caller.prototype.updatePortfolio = function(portfolio) {
	// portfolio.content is already json string

	$.ajax({
		async : false,
		type : 'PUT',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		contentType : 'application/json',
		data : portfolio.content,
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.Portfolio/' + portfolio.id,
		beforeSend : function() {
			// $('#overview').append($('<div></div>').addClass('icon loader'));
		},
		success : function(data, textStatus, jqXHR) {
			__Caller.getPortfolios('#tab_menu');
			__Caller.refresh();
			__Utils.refreshUI();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			// alert(textStatus + ' ' + errorThrown);
		}
	});
};

Caller.prototype.deletePortfolio = function(id) {
	$.ajax({
		async : false,
		type : 'DELETE',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.Portfolio/' + id,
		success : function(data, textStatus, jqXHR) {
			__MyPortfoliosCurrent == null;
			__Main.go('myportfolios');
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

/*
 * UserData (Notes!)
 */
Caller.prototype.getUserData = function(containerSelector) {
	$.ajax({
		type : 'GET',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/eu.trentorise.smartcampus.portfolio.models.UserData',
		success : function(data, textStatus, jqXHR) {
			var renderer = new Renderer_Notes();
			renderer.render(containerSelector, data);
			__Utils.refreshUI();
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

Caller.prototype.updateUserData = function(id, data) {
	/*
	 * category, type, title, subtitle, content
	 */
	var json = JSON.stringify(data);

	$.ajax({
		async : false,
		type : 'PUT',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		contentType : 'application/json',
		data : json,
		url : __Properties.baseUrl + 'rest/eu.trentorise.smartcampus.portfolio.models.UserData/' + id,
		success : function(data, textStatus, jqXHR) {
			__EditMode = false;
			__EditMode_change = false;
			__Renderer_Toolbar.closeSaveBtn();
			__Main.go('notes');
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

/*
 * Exams
 */
Caller.prototype.getStudentExams = function(containerSelector) {
	$.ajax({
		type : 'GET',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/smartcampus.services.esse3.StudentExams',
		success : function(data, textStatus, jqXHR) {
			var renderer = new Renderer_StudentExams();
			renderer.render(containerSelector, data);
			__Utils.refreshUI();
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

/*
 * Profile
 */
Caller.prototype.getProfile = function() {
	$.ajax({
		async : false,
		type : 'GET',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		url : __Properties.baseUrl + 'rest/getprofile',
		success : function(data, textStatus, jqXHR) {
			if (data == '') {
				__Main.go('profilenew');
			} else {
				__Profile = JSON.parse(data);
				__Caller.createStudent();
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

Caller.prototype.createProfile = function(data) {
	var json = JSON.stringify(data);

	$.ajax({
		async : false,
		type : 'POST',
		headers : {
			'AUTH_TOKEN' : __Sec.getToken()
		},
		contentType : 'application/json',
		data : json,
		url : __Properties.baseUrl + 'rest/createprofile',
		success : function(data, textStatus, jqXHR) {
			__Caller.getProfile();
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
};

Caller.prototype.execNewProfile = function() {
	var name = $('#profilenew form #name').val();
	var surname = $('#profilenew form #surname').val();

	if (name.length == 0 || surname.length == 0) {
		alert('Name and surname cannot be empty');
		return;
	}

	var data = {
		'name' : name,
		'surname' : surname
	};

	this.createProfile(data);
};

/*
 * General
 */
Caller.prototype.refresh = function(categories) {
	__MyPortfoliosCurrentCherries = [];
	__MyPortfoliosCurrentCherriesDone = false;

	if (categories == undefined || categories == null || categories.length == 0) {
		categories = [ 'overview', 'language', 'skill', 'contact', 'education', 'presentation', 'professional', 'about' ];
	}

	for ( var i = 0; i < categories.length; i++) {
		var category = categories[i];
		if (category == 'overview') {
			this.getOverview();
			// this.getOverviewUserContent('language', '#overview_languages');
			// this.getOverviewUserContent('skill', '#overview_skills');
			// this.getOverviewUserContent('contact', '#overview_contacts');
		} else if (category == 'language') {
			this.getOverviewUserContent('language', '#overview_languages');
		} else if (category == 'skill') {
			this.getOverviewUserContent('skill', '#overview_skills');
		} else if (category == 'contact') {
			this.getOverviewUserContent('contact', '#overview_contacts');
		} else if (category == 'education' || category == 'presentation' || category == 'professional'
				|| category == 'about') {
			this.getUserContent(category);
		}
	}
};

Caller.prototype.download_pdf = function() {
	// fetches BINARY FILES synchronously using XMLHttpRequest
	var req = new XMLHttpRequest();
	req.open('GET', __Properties.baseUrl + 'rest/generatecv/' + __MyPortfoliosCurrent.id + '/pdf/true', false);
	req.setRequestHeader("AUTH_TOKEN", __Sec.getToken());
	// XHR binary charset opt by Marcus Granado 2006 [http://mgran.blogspot.com]
	req.overrideMimeType('text/plain; charset=x-user-defined');
	req.send(null);
	if (req.status != 200)
		return '';
	return req.responseText;
};

// Caller.prototype.getUserContentById = function(id) {
// $.ajax({
// type : 'GET',
// async : false,
// url : __Properties.baseUrl + '/getuserdatabyid/' + id,
// success : function(data, textStatus, jqXHR) {
// return data;
// },
// error : function(jqXHR, textStatus, errorThrown) {
// return null;
// }
// });
// };

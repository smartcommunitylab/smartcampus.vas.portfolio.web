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
function Renderer_Overview() {
};

Renderer_Overview.prototype.render = function(data) {
	// renderer
	var renderer = new Renderer_StudentInfo();

	// card
	var card = $('#overview_card');
	$('#overview_card').empty();

	var json;
	if (data == '') {
		data = JSON.stringify({
			'content' : JSON.stringify({})
		});
	}

	json = JSON.parse(data);
	content = JSON.parse(json['content']);
	var studentData = null;
	if (__IsStudent) {
		studentData = content['studentData'];
	}
	__StudentData = studentData;

	if (__Current == 'myportfolios' && __EditMode == false && __IsStudent == true && __Utils.valid(studentData)) {
		var studentDataFields = __Properties.studentInfos; // TEST
		var mpc = JSON.parse(__MyPortfoliosCurrent.content);

		for ( var s = 0; s < studentDataFields.length; s++) {
			var sdf = studentDataFields[s];
			if ($.inArray(sdf, mpc['showStudentInfo']) == -1) {
				var sdfs = sdf.split('_');
				for ( var sdfsc = 0; sdfsc < sdfs.length; sdfsc++) {
					studentData[sdfs[sdfsc]] = null;
				}
			}
		}

		// for ( var s = 0; s < studentDataFields.length; s++) {
		// var sdf = studentDataFields[s].split('_');
		// for ( var sdfcounter = 0; sdfcounter < sdf.length; sdfcounter++) {
		// if ($.inArray(sdf[sdfcounter], mpc['showStudentInfo']) == -1) {
		// studentData[sdf[sdfcounter]] = null;
		// }
		// }
		// }
	}

	// TODO: per foto fare chiamata UserGeneratedContent
	var photoDiv = $('<div></div>').addClass('overview_photo');
	var photoTools = [ 'edit' ];
	var photo = $('<img />');
	photo.attr('alt', __Profile['name'] + ' ' + __Profile['surname']);
	if (__Utils.valid(studentData) && __Utils.valid(studentData['photo'])) {
		photo.attr('src', studentData['photo']);
		photoTools.push('delete');
		photoTools.reverse();
	} else {
		photo.attr('src', 'img/photo.png');
	}

	// var photo_tools = __Renderer_Tools.render(null, photoTools);
	// photoDiv.append(photo_tools, photo);
	photoDiv.append(photo);

	var nextToPhoto = $('<div></div>').addClass('text');
	var text = renderer.render(null, '', __Profile['name'] + ' ' + __Profile['surname']);
	text.addClass('overview_name');
	nextToPhoto.append(text);

	if (__Utils.valid(studentData) && __Utils.valid(studentData['cds'])) {
		text = renderer.render('cds', 'Current occupation:', studentData['cds']); // TODO
		nextToPhoto.append(text);
	}

	if (__Utils.valid(studentData) && __Utils.valid(studentData['academicYear'])) {
		var string = studentData['academicYear'];
		if (__Utils.valid(studentData['supplementaryYears'])) {
			string += ' + ' + studentData['supplementaryYears'];
		}
		text = renderer.render('academicYear', 'Academic year:', string);
		nextToPhoto.append(text);
	}

	if (__Utils.valid(studentData) && __Utils.valid(studentData['enrollmentYear'])) {
		text = renderer.render('enrollmentYear', 'Enrollment year:', studentData['enrollmentYear']);
		nextToPhoto.append(text);
	}

	if (__Utils.valid(studentData) && __Utils.valid(studentData['cfu'])) {
		text = renderer.render('cfu_cfuTotal', 'CFU:', studentData['cfu'] + ' / ' + studentData['cfuTotal']);
		nextToPhoto.append(text);
	}

	if (__Utils.valid(studentData) && __Utils.valid(studentData['marksNumber'])) {
		text = renderer.render('marksNumber', 'Marks:', studentData['marksNumber']);
		nextToPhoto.append(text);
	}

	if (__Utils.valid(studentData) && __Utils.valid(studentData['marksAverage'])) {
		text = renderer.render('marksAverage', 'Average grade:', studentData['marksAverage']);
		nextToPhoto.append(text);
	}

	card.append(photoDiv, nextToPhoto);

	// personal data
	var personal = $('#overview_personal').addClass('overview_sub');
	personal.empty();
	var list = $('<ul></ul>').addClass('overview_sub_large');
	var li = $('<li></li>');
	if (__Utils.valid(studentData) && __Utils.valid(studentData['gender'])) {
		text = renderer.render('gender', 'Gender:', studentData['gender']);
		li.append(text.children());
		list.append(li);
	}

	if (__Utils.valid(studentData) && __Utils.valid(studentData['dateOfBirth'])) {
		li = $('<li></li>');
		text = renderer.render('dateOfBirth', 'Birthday:', studentData['dateOfBirth']);
		li.append(text.children());
		list.append(li);
	}

	if (__Utils.valid(studentData) && __Utils.valid(studentData['nation'])) {
		li = $('<li></li>');
		text = renderer.render('nation', 'Citizenship:', studentData['nation']);
		li.append(text.children());
		list.append(li);
	}
	personal.append(list);

	// languages
	var languages = $('#overview_languages').addClass('overview_sub');
	var languagesHeader = $(languages).find('.overview_sub_header');
	languagesHeader.empty().append($('<span></span>').text('Languages'));
	var actions = $('<div></div>').addClass('overview_sub_header_actions');
	var action;
	if (__Current == 'manager') {
		action = $('<a></a>').text('+Add');
		action.click(function() {
			__Forms.form('languages', languages);
		});
		actions.append(action);
	}
	languagesHeader.append(actions);

	// skills
	var skills = $('#overview_skills').addClass('overview_sub');
	var skillsHeader = $(skills).find('.overview_sub_header');
	skillsHeader.empty().append($('<span></span>').text('Skills'));
	var actions = $('<div></div>').addClass('overview_sub_header_actions');
	if (__Current == 'manager') {
		action = $('<a></a>').text('+Add');
		action.click(function() {
			__Forms.form('skills', skills);
		});
		actions.append(action);
	}
	skillsHeader.append(actions);

	// contacts
	var contacts = $('#overview_contacts').addClass('overview_sub');
	var contactsHeader = $(contacts).find('.overview_sub_header');
	contactsHeader.empty().append($('<span></span>').text('Contacts'));
	var actions = $('<div></div>').addClass('overview_sub_header_actions');
	if (__Current == 'manager') {
		action = $('<a></a>').text('+Add');
		action.click(function() {
			__Forms.form('contacts', contacts);
		});
		actions.append(action);
	}
	contactsHeader.append(actions);

	var contactsList = $(contacts).find('ul');
	contactsList.empty();

	if (__Utils.valid(studentData) && __Utils.valid(studentData['address'])) {
		li = $('<li></li>');
		text = (renderer.render('address', 'Address:', studentData['address'])).children();
		li.append(text);
		contactsList.append(li);
	}

	// if (__Utils.valid(studentData) && __Utils.valid(studentData['email'])) {
	// li = $('<li></li>');
	// text = (renderer.render('email', 'Email:',
	// studentData['email'])).children();
	// li.append(text);
	// contactList.append(li);
	// }

	if (__Utils.valid(studentData) && __Utils.valid(studentData['phone'])) {
		li = $('<li></li>');
		text = (renderer.render('phone', 'Phone:', studentData['phone'])).children();
		li.append(text);
		contactsList.append(li);
	}

	if (__Utils.valid(studentData) && __Utils.valid(studentData['mobile'])) {
		li = $('<li></li>');
		text = (renderer.render('mobile', 'Mobile:', studentData['mobile'])).children();
		li.append(text);
		contactsList.append(li);
	}
};

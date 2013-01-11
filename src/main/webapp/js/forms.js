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
function Forms() {
};

/* OVERVIEW */
Forms.prototype.formLanguages = function(container) {
	var ul = $(container).find('ul');
	var form = $('<form></form>').addClass('form');
	var input1 = $('<input/>').addClass('input_language').attr('type', 'text').attr('name', 'title').attr(
			'placeholder', 'Language');
	var input2 = $('<input/>').addClass('input_level').attr('type', 'text').attr('name', 'subtitle').attr(
			'placeholder', 'Level');
	form.append(input1, input2);

	var buttonOK = $('<input/>').attr('type', 'submit').val('OK');
	var buttonCancel = $('<input/>').attr('type', 'button').val('Cancel');
	buttonCancel.click(function() {
		$(container).find('.form').remove();
	});

	form.append('<br/>');
	form.append(buttonOK);
	form.append(buttonCancel);

	form.submit(function(e) {
		e.preventDefault();
		var data = {};
		data['category'] = 'language';
		data['type'] = 'language';
		data['title'] = $('.input_language').val();
		data['subtitle'] = $('.input_level').val();
		data['content'] = '';
		__Caller.createUserContent('language', data);
	});

	var li = $('<li></li>').addClass('overview_userdata').append(form);
	ul.append(li);
	input1.focus();
};

Forms.prototype.formSkills = function(container) {
	var ul = $(container).find('ul');
	var form = $('<form></form>').addClass('form');
	var input1 = $('<input/>').addClass('input_skill').attr('type', 'text').attr('name', 'title').attr('placeholder',
			'Skill');
	form.append(input1);
	var buttonOK = $('<input/>').attr('type', 'submit').val('OK');
	var buttonCancel = $('<input/>').attr('type', 'button').val('Cancel');
	buttonCancel.click(function() {
		$(container).find('.form').remove();
	});

	form.append('<br/>');
	form.append(buttonOK);
	form.append(buttonCancel);

	form.submit(function(e) {
		e.preventDefault();
		var data = {};
		data['category'] = 'skill';
		data['type'] = 'skill';
		data['title'] = $('.input_skill').val();
		data['subtitle'] = '';
		data['content'] = '';
		__Caller.createUserContent('skill', data);
	});

	var li = $('<li></li>').addClass('overview_userdata').append(form);
	ul.append(li);
	input1.focus();
};

Forms.prototype.formContacts = function(container) {
	var ul = $(container).find('ul');
	var form = $('<form></form>').addClass('form');
	var input1 = $('<input/>').addClass('input_contacttype').attr('type', 'text').attr('name', 'title').attr(
			'placeholder', 'Type');
	var input2 = $('<input/>').addClass('input_contactvalue').attr('type', 'text').attr('name', 'subtitle').attr(
			'placeholder', 'Contact');
	form.append(input1, input2);

	var buttonOK = $('<input/>').attr('type', 'submit').val('OK');
	var buttonCancel = $('<input/>').attr('type', 'button').val('Cancel');
	buttonCancel.click(function() {
		$(container).find('.form').remove();
	});

	form.append('<br/>');
	form.append(buttonOK);
	form.append(buttonCancel);

	form.submit(function(e) {
		e.preventDefault();
		var data = {};
		data['category'] = 'contact';
		data['type'] = 'contact';
		data['title'] = $('.input_contacttype').val();
		data['subtitle'] = $('.input_contactvalue').val();
		data['content'] = '';
		__Caller.createUserContent('contact', data);
	});

	var li = $('<li></li>').addClass('overview_userdata').append(form);
	ul.append(li);
	input1.focus();
};

/* SECTION */
Forms.prototype.formRaw = function(category, container, editEntry) {
	var form = $('<form></form>').addClass('form section_form raw');
	var input_content = $('<textarea/>').addClass('input_content').attr('name', 'content').attr('required', 'required');
	form.append(input_content);

	// edit
	if (editEntry != undefined && editEntry != null) {
		var content = JSON.parse(editEntry.content);
		input_content.val(content.content);
	}

	var buttonOK = $('<input/>').attr('type', 'submit').val('OK');
	var buttonCancel = $('<input/>').attr('type', 'button').val('Cancel');
	buttonCancel.click(function() {
		__Forms.cancel(category, container);
	});

	form.append(buttonOK);
	form.append(buttonCancel);

	form.submit(function(e) {
		e.preventDefault();
		var data = {};
		data['category'] = category;
		data['type'] = 'raw';
		data['title'] = '';
		data['subtitle'] = '';
		data['content'] = $('.input_content', form).val();

		if (data['content'] != '') {
			if (editEntry == undefined || editEntry == null) {
				__Caller.createUserContent(category, data);
			} else {
				__Caller.updateUserContent(category, editEntry.id, data);
			}
		}
	});

	container.append(form);
	input_content.focus();
};

Forms.prototype.formSimple = function(category, container, editEntry) {
	var placeholderTitle = '';
	var placeholderSubtitle = '';

	if (category == 'education') {
		placeholderTitle = 'Education title';
		placeholderSubtitle = 'Time period';
	}

	var form = $('<form></form>').addClass('form section_form simple');
	var input_title = $('<input/>').addClass('input_title').attr('type', 'text').attr('name', 'title').attr(
			'placeholder', placeholderTitle).attr('required', 'required');
	var input_subtitle = $('<input/>').addClass('input_subtitle').attr('type', 'text').attr('name', 'subtitle').attr(
			'placeholder', placeholderSubtitle);
	form.append(input_title);
	form.append(input_subtitle);

	// edit
	if (editEntry != undefined && editEntry != null) {
		var content = JSON.parse(editEntry.content);
		input_title.val(content.title);
		input_subtitle.val(content.subtitle);
	}

	var buttonOK = $('<input/>').attr('type', 'submit').val('OK');
	var buttonCancel = $('<input/>').attr('type', 'button').val('Cancel');
	buttonCancel.click(function() {
		__Forms.cancel(category, container);
	});

	form.append(buttonOK);
	form.append(buttonCancel);

	form.submit(function(e) {
		e.preventDefault();
		var data = {};
		data['category'] = category;
		data['type'] = 'simple';
		data['title'] = $('.input_title', form).val();
		data['subtitle'] = $('.input_subtitle', form).val();
		data['content'] = '';

		if (data['title'] != '' && data['subtitle'] != '') {
			if (editEntry == undefined || editEntry == null) {
				__Caller.createUserContent(category, data);
			} else {
				__Caller.updateUserContent(category, editEntry.id, data);
			}
		}
	});

	container.append(form);
	input_title.focus();
};

Forms.prototype.formSimpleDesc = function(category, container, editEntry) {
	var placeholderTitle = '';
	var placeholderSubtitle = '';
	var placeholderContent = '';

	if (category == 'professional') {
		placeholderTitle = 'Experience';
		placeholderSubtitle = 'Time period';
		placeholderContent = 'Description';
	}

	var form = $('<form></form>').addClass('form section_form simple');
	var input_title = $('<input/>').addClass('input_title').attr('type', 'text').attr('name', 'title').attr(
			'placeholder', placeholderTitle).attr('required', 'required');
	var input_subtitle = $('<input/>').addClass('input_subtitle').attr('type', 'text').attr('name', 'subtitle').attr(
			'placeholder', placeholderSubtitle);
	var input_content = $('<textarea/>').addClass('input_content').attr('name', 'content').attr('placeholder',
			placeholderContent);
	form.append(input_title);
	form.append(input_subtitle);
	form.append(input_content);

	// edit
	if (editEntry != undefined && editEntry != null) {
		var content = JSON.parse(editEntry.content);
		input_title.val(content.title);
		input_subtitle.val(content.subtitle);
		input_content.val(content.content);
	}

	var buttonOK = $('<input/>').attr('type', 'submit').val('OK');
	var buttonCancel = $('<input/>').attr('type', 'button').val('Cancel');
	buttonCancel.click(function() {
		__Forms.cancel(category, container);
	});

	form.append(buttonOK);
	form.append(buttonCancel);

	form.submit(function(e) {
		e.preventDefault();
		var data = {};
		data['category'] = category;
		data['type'] = 'simple_desc';
		data['title'] = $('.input_title', form).val();
		data['subtitle'] = $('.input_subtitle', form).val();
		data['content'] = $('.input_content', form).val();

		if (data['title'] != '' && data['subtitle'] != '' && data['content'] != '') {
			if (editEntry == undefined || editEntry == null) {
				__Caller.createUserContent(category, data);
			} else {
				__Caller.updateUserContent(category, editEntry.id, data);
			}
		}
	});

	container.append(form);
	input_title.focus();
};

Forms.prototype.formSimplePic = function(category, container, editEntry) {
	var placeholderTitle = '';
	var placeholderSubtitle = '';
	var placeholderContent = '';

	if (category == 'about') {
		placeholderTitle = 'Title';
		placeholderSubtitle = 'Comment';
		placeholderContent = '...Coming soon...';
	}

	var form = $('<form></form>').addClass('form section_form simple');
	var input_title = $('<input/>').addClass('input_title').attr('type', 'text').attr('name', 'title').attr(
			'placeholder', placeholderTitle);
	var input_subtitle = $('<input/>').addClass('input_subtitle').attr('type', 'text').attr('name', 'subtitle').attr(
			'placeholder', placeholderSubtitle);
	var input_content = $('<input/>').addClass('input_content').attr('type', 'text').attr('name', 'content').attr(
			'placeholder', placeholderContent);
	form.append(input_title);
	form.append(input_subtitle);
	form.append(input_content);

	// edit
	if (editEntry != undefined && editEntry != null) {
		var content = JSON.parse(editEntry.content);
		input_title.val(content.title);
		input_subtitle.val(content.subtitle);
		input_content.val(content.content);
	}

	var buttonOK = $('<input/>').attr('type', 'submit').val('OK');
	var buttonCancel = $('<input/>').attr('type', 'button').val('Cancel');
	buttonCancel.click(function() {
		__Forms.cancel(category, container);
	});

	form.append(buttonOK);
	form.append(buttonCancel);

	form.submit(function(e) {
		e.preventDefault();
		var data = {};
		data['category'] = category;
		data['type'] = 'simple_pic';
		data['title'] = $('.input_title', form).val();
		data['subtitle'] = $('.input_subtitle', form).val();
		// data['content'] = $('.input_content', form).val();
		// data['content'] = 'http://placekitten.com/g/80/80';
		data['content'] = __Properties.baseUrl + 'img/' + 'ls_logo_grey.png';

		if (data['title'] != '' && data['subtitle'] != '' && data['content'] != '') {
			if (editEntry == undefined || editEntry == null) {
				__Caller.createUserContent(category, data);
			} else {
				__Caller.updateUserContent(category, editEntry.id, data);
			}
		}
	});

	container.append(form);
	input_title.focus();
};

Forms.prototype.formVideo = function(category, container, editEntry) {
	var placeholderContent = '';

	if (category == 'presentation') {
		placeholderContent = 'Video link, e.g.: http://www.youtube.com/watch?v=w0ffwDYo00Q)';
	}

	var form = $('<form></form>').addClass('form section_form simple');
	var input_content = $('<input/>').addClass('input_content').attr('type', 'text').attr('name', 'content').attr(
			'placeholder', placeholderContent);
	form.append(input_content);

	// edit
	if (editEntry != undefined && editEntry != null) {
		var content = JSON.parse(editEntry.content);
		input_content.val(content.content);
	}

	var buttonOK = $('<input/>').attr('type', 'submit').val('OK');
	var buttonCancel = $('<input/>').attr('type', 'button').val('Cancel');
	buttonCancel.click(function() {
		__Forms.cancel(category, container);
	});

	form.append(buttonOK);
	form.append(buttonCancel);

	form.submit(function(e) {
		e.preventDefault();
		var data = {};
		data['category'] = category;
		data['type'] = 'video';
		data['title'] = '';
		data['subtitle'] = '';
		data['content'] = $('.input_content', form).val();

		if (data['content'] != '') {
			if (editEntry == undefined || editEntry == null) {
				__Caller.createUserContent(category, data);
			} else {
				__Caller.updateUserContent(category, editEntry.id, data);
			}
		}
	});

	container.append(form);
	input_content.focus();
};

Forms.prototype.formNewPortfolio = function(containerSelector) {
	var form = $('<form></form>').addClass('form');
	var input1 = $('<input/>').addClass('input_newportfolioname').attr('type', 'text').attr('name', 'newportfolioname')
	.attr('placeholder', 'New portfolio name'); 
	form.append(input1);

	var buttonOK = $('<input/>').attr('type', 'submit').val('OK');
	var buttonCancel = $('<input/>').attr('type', 'button').val('Cancel');
	buttonCancel.click(function(e) {
		$(containerSelector).find('.form').remove();
		e.stopPropagation();
		// __Caller.refresh();
	});

	form.append(buttonCancel);
	form.append(buttonOK);

	form.submit(function(e) {
		e.preventDefault();
		var data = {};
		data['name'] = $('.input_newportfolioname').val();
		__Caller.createPortfolio(data);
		$(containerSelector).find('.form').remove();
	});

	$(containerSelector).append(form);
	input1.focus();
};

Forms.prototype.formNotes = function(containerSelector) {
	__EditMode = true;

	var form = $('<form></form>').addClass('form');
	var input_content = $('<textarea/>').addClass('input_content').attr('name', 'content').attr('required', 'required');
	input_content.text(__Notes.notes);

	input_content.bind('input propertychange', function() {
		if (__EditMode_change != true) {
			__EditMode_change = true;
			__Renderer_Toolbar.enableSaveBtn();
			$(this).unbind('input propertychange');
		}
	});

	form.append(input_content);
	$(containerSelector).empty();
	$(containerSelector).append(form);
	input_content.focus();
};

Forms.prototype.formChoose = function(category, container, editEntry) {
	var chooseDiv = $('<div></div>').addClass('form choose');

	var options = __Properties.categories[category].choose;

	for ( var i = 0; i < options.length; i++) {
		var option = options[i];

		var a = $('<a></a>');
		if (option == 'raw') {
			a.text('Text');
			a.click(function() {
				$(container).find('.form').remove();
				__Forms.formRaw(category, container, editEntry);
			});
		} else if (option == 'simple') {
			a.text('Simple');
			a.click(function() {
				$(container).find('.form').remove();
				__Forms.formSimple(category, container, editEntry);
			});
		} else if (option == 'simple_desc') {
			a.text('Simple with description');
			a.click(function() {
				$(container).find('.form').remove();
				__Forms.formSimpleDesc(category, container, editEntry);
			});
		} else if (option == 'simple_pic') {
			a.text('Simple with picture');
			a.click(function() {
				$(container).find('.form').remove();
				__Forms.formSimplePic(category, container, editEntry);
			});
		} else if (option == 'video') {
			a.text('Video');
			a.click(function() {
				$(container).find('.form').remove();
				__Forms.formVideo(category, container, editEntry);
			});
		} else {
			a = $('<span></span>').text(option);
		}

		chooseDiv.append(a);

		if (options[i + 1] != undefined && options[i + 1] != null) {
			chooseDiv.append(', ');
		} else {
			chooseDiv.append(' or ');
			var linkCancel = $('<a></a>').text('Cancel');
			linkCancel.click(function() {
				__Forms.cancel(category, container);
			});
			chooseDiv.append(linkCancel);
		}
	}

	container.append(chooseDiv);
};

Forms.prototype.form = function(category, container, editEntry) {
	if ($(container).find('.form').length == 0) {
		if (category == 'portfolio') {
			this.formNewPortfolio(container);
		} else if (category == 'notes') {
			this.formNotes(container);
		} else if (category == 'languages') {
			this.formLanguages(container);
		} else if (category == 'skills') {
			this.formSkills(container);
		} else if (category == 'contacts') {
			this.formContacts(container);
		} else {
			// from section
			var options = __Properties.categories[category].choose;
			if (options.length > 1) {
				this.formChoose(category, container, editEntry);
			} else if (options.length == 1) {
				var option = options[0];

				if (option == 'raw') {
					this.formRaw(category, container, editEntry);
				} else if (option == 'simple') {
					this.formSimple(category, container, editEntry);
				} else if (option == 'simple_desc') {
					this.formSimpleDesc(category, container, editEntry);
				} else if (option == 'simple_pic') {
					this.formSimplePic(category, container, editEntry);
				} else if (option == 'video') {
					this.formVideo(category, container, editEntry);
				}
			}
		}
	}
};

Forms.prototype.cancel = function(category, container) {
	$(container).find('.form').remove();
	__Caller.refresh([ category ]);
};

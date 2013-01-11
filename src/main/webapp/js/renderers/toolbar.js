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
function Renderer_Toolbar() {
};

Renderer_Toolbar.prototype.render = function() {
	// var toolbar = $('#toolbar');
	var ul = $('#toolbar .toolbar_tools');
	var div_tags = $('#toolbar .toolbar_tags');
	var divBtn = $('#toolbar .toolbar_buttons');
	var li;
	var a;
	var iconspan;
	var span;

	$(ul).empty();

	$(divBtn).empty();
	$(divBtn).hide();

	$(div_tags).empty();
	$(div_tags).hide();

	if (__Current == 'empty') {
		// nothing
	} else if (__Current == 'manager') {
		// TODO
		// iconspan = $('<span></span>').addClass('icon ib add_section');
		// span = $('<span></span>').text('[Placeholder]');
		// a = $('<a></a>').attr('href', '#');
		// a.append(iconspan, span);
		// li = $('<li></li>');
		// li.append(a);
		// ul.append(li);

		// iconspan = $('<span></span>').addClass('icon ib import');
		// span = $('<span></span>').text('Import external data');
		// a = $('<a></a>').attr('href', '#');
		// a.append(iconspan, span);
		// li = $('<li></li>');
		// li.append(a);
		// ul.append(li);

		// iconspan = $('<span></span>').addClass('icon ib add_section');
		// span = $('<span></span>').text('Add section');
		// a = $('<a></a>').attr('href', '#');
		// a.append(iconspan, span);
		// li = $('<li></li>');
		// li.append(a);
		// ul.append(li);
	} else if (__Current == 'myportfolios') {
		// radio buttons (edit / preview)
		var epForm = $('<form></form>');

		var editRB = $('<input />').attr('type', 'radio').attr('name', 'editmode').attr('id', 'rb_edit').val('edit');
		if (__EditMode) {
			editRB.attr('checked', 'checked');
		}
		editRB.click(function() {
			__EditMode = true;
			if ($('#rb_edit').is(':checked')) {
				__Main.go('myportfolios');
			}
		});
		var editLabel = $('<label></label>').attr('for', 'rb_edit').text('Edit');
		epForm.append(editRB);
		epForm.append(editLabel);

		var previewRB = $('<input />').attr('type', 'radio').attr('name', 'editmode').attr('id', 'rb_preview').val(
				'preview');
		if (!__EditMode || __EditMode == null) {
			previewRB.attr('checked', 'checked');
		}
		previewRB.click(function() {
			__EditMode = false;
			if ($('#rb_preview').is(':checked')) {
				// TODO render preview mode
				__Main.go('myportfolios');
			}
		});
		var previewLabel = $('<label></label>').attr('for', 'rb_preview').text('Preview');
		epForm.append(previewRB);
		epForm.append(previewLabel);

		li = $('<li></li>');
		li.append(epForm);
		ul.append(li);

		// PDF export
		iconspan = $('<span></span>').addClass('icon ib export');
		span = $('<span></span>').text('PDF export');
		a = $('<a></a>');
		a.attr('id', 'download_cv_pdf');
		a.attr('href', __Properties.baseUrl + 'rest/generatecv/' + __MyPortfoliosCurrent.id + '/pdf/'
				+ __Sec.getToken());
		a.click(function() {
			var pdfContent = __Caller.download_pdf();
			$(this).attr('href', 'data:application/pdf;base64,' + encodeURIComponent(pdfContent));
		});
		a.append(iconspan, span);
		li = $('<li></li>');
		li.append(a);
		ul.append(li);

		// save button
		var aBtn = $('<a></a>').addClass('btn');
		aBtn.text('Save');
		aBtn.click(function() {
			var myPortfoliosCurrentContent = JSON.parse(__MyPortfoliosCurrent.content);
			myPortfoliosCurrentContent.showStudentInfo = [];
			myPortfoliosCurrentContent.showUserGeneratedData = [];
			myPortfoliosCurrentContent.highlightUserGeneratedData = [];

			var eyesArray = $('#main .eye');
			for ( var c = 0; c < eyesArray.length; c++) {
				var eye = eyesArray[c];
				if ($.inArray(eye.id, __Properties.studentInfos) > -1) {
					// var splittedId = eye.id.split('_');
					// for ( var s = 0; s < splittedId.length; s++) {
					// myPortfoliosCurrentContent.showStudentInfo.push(splittedId[s]);
					// }
					myPortfoliosCurrentContent.showStudentInfo.push(eye.id);
				} else {
					myPortfoliosCurrentContent.showUserGeneratedData.push(eye.id);
				}
			}

			var cherriesArray = $('#main .cherry');
			for ( var c = 0; c < cherriesArray.length; c++) {
				var cherry = cherriesArray[c];
				myPortfoliosCurrentContent.highlightUserGeneratedData.push(cherry.id);
			}

			__MyPortfoliosCurrent.content = JSON.stringify(myPortfoliosCurrentContent);
			__Caller.updatePortfolio(__MyPortfoliosCurrent);
			__EditMode_change = false;
			__Renderer_Toolbar.closeSaveBtn();
		});
		divBtn.append(aBtn);

		// tags
		// { "id" : null , "name" : "university" , "description" : null ,
		// "summary" : null }
		var myPortfoliosCurrentContent = JSON.parse(__MyPortfoliosCurrent.content);
		var tags = myPortfoliosCurrentContent.tags;
		if (__Utils.valid(tags) && tags.length > 0) {
			// TODO create tags views
			var ul_tags = $('<ul></ul>');
			for ( var t = 0; t < tags.length; t++) {
				var tag = tags[t];
				var li_tag = $('<li></li>').addClass('tag');
				li_tag.append(tag.name);
				ul_tags.append(li_tag);
			}

			div_tags.append($('<span></span>').append('Tags: '));
			div_tags.append(ul_tags);
			$(div_tags).show();
		}
	} else if (__Current == 'noticeboard') {
		// TODO
	} else if (__Current == 'notes') {
		// TODO
		// Edit note
		iconspan = $('<span></span>').addClass('icon ib edit');
		span = $('<span></span>').text('Edit notes');
		a = $('<a></a>').append(iconspan, span);
		a.click(function() {
			__Forms.form('notes', '#notes', null);
		});
		li = $('<li></li>');
		li.append(a);
		ul.append(li);

		// save button
		var aBtn = $('<a></a>').addClass('btn');
		aBtn.text('Save');
		aBtn.click(function() {
			var newNotes = {
				'notes' : $('#notes textarea.input_content').val()
			};
			__Caller.updateUserData(__Notes.id, newNotes);
		});
		divBtn.append(aBtn);
	} else if (__Current == 'exams') {
		// TODO
		// iconspan = $('<span></span>').addClass('icon ib add_section');
		// span = $('<span></span>').text('[Placeholder]');
		// a = $('<a></a>').attr('href', '#');
		// a.append(iconspan, span);
		// li = $('<li></li>');
		// li.append(a);
		// ul.append(li);
	}

	// if ($(ul).children().length == 0) {
	// $(ul).hide();
	// }
};

Renderer_Toolbar.prototype.enableSaveBtn = function() {
	if (__Current == 'myportfolios' || __Current == 'notes') {
		$('#toolbar .toolbar_buttons').slideDown();
	}
};

Renderer_Toolbar.prototype.closeSaveBtn = function() {
	$('#toolbar .toolbar_buttons').slideUp();
};

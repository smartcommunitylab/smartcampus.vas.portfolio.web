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
function Renderer_Section() {
};

Renderer_Section.prototype.render = function(category, data) {
	var section = $(__Properties.categories[category].containerSelector);

	section.empty();

	var entriesArray = [];

	if (category == 'cherryotc') {
		entriesArray = __Utils.cleanDuplicates(__MyPortfoliosCurrentCherries);
	} else {
		entriesArray = JSON.parse(data);
		// category = __Utils.firstUppercase(category);
	}

	if (category != 'cherryotc' && __Current == 'myportfolios' && __EditMode == false) {
		//var mpc = JSON.parse(__MyPortfoliosCurrent.content);
		var mpc = __MyPortfoliosCurrent.content;
		entriesArray = __Utils.filterEntries(entriesArray, mpc['showUserGeneratedData'], 'id');

		if (entriesArray.length == 0) {
			return;
		}
	}

	var headerTitle = '';
	if (category == 'education') {
		headerTitle = 'Education';
	} else if (category == 'presentation') {
		headerTitle = 'Presentation';
	} else if (category == 'professional') {
		headerTitle = 'Professional Experiences';
	} else if (category == 'about') {
		headerTitle = 'More about me';
	} else if (category == 'cherryotc') {
		headerTitle = 'Cherry on the cake';
	}

	var header = $('<div></div>').addClass('section_header');
	var title = $('<span></span>').text(headerTitle);
	header.append(title);
	section.append(header);

	var sectionBody = $('<div></div>').addClass('section_body');
	var sectionOUC = $('<ul></ul>');
	for ( var i = 0; i < entriesArray.length; i++) {
		var entry = entriesArray[i];
		//var content = JSON.parse(entry.content);
		var content = entry.content;
		var renderer = null;
		if (content.type == 'language') {
			renderer = new Renderer_Entry_language();
		} else if (content.type == 'skill') {
			renderer = new Renderer_Entry_skill();
		} else if (content.type == 'contact') {
			renderer = new Renderer_Entry_contact();
		} else if (content['type'] == 'raw') {
			renderer = new Renderer_Entry_raw();
		} else if (content['type'] == 'simple') {
			renderer = new Renderer_Entry_simple();
		} else if (content['type'] == 'simple_desc') {
			renderer = new Renderer_Entry_simple_desc();
		} else if (content['type'] == 'simple_pic') {
			renderer = new Renderer_Entry_simple_pic();
		} else if (content['type'] == 'video') {
			renderer = new Renderer_Entry_video();
		} else if (content['type'] == 'sys_simple') {
			renderer = new Renderer_Entry_SYS_simple();
		}

		if (renderer != null) {
			if (category == 'cherryotc'
					&& (content.type == 'language' || content.type == 'skill' || content.type == 'contact')) {
				sectionOUC.append(renderer.render(entry));
			} else {
				var entryDiv = renderer.render(entry);
				sectionBody.append(entryDiv);
				sectionBody.append($('<div></div>').addClass('clear'));
			}
		}
	}

	if ($(sectionOUC).html().trim() != '') {
		sectionBody.append(sectionOUC);
	}

	section.append(sectionBody);

	var footer = $('<div></div>').addClass('section_footer');
	if (__Current == 'manager') {
		var action = $('<a></a>').text('+Add');
		action.click(function() {
			__Forms.form(category, sectionBody);
		});
		footer.append(action);
	}
	section.append(footer);

	section.append($('<div></div>').addClass('clear'));
};

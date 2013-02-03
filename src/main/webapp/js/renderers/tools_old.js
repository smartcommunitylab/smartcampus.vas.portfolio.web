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
function Renderer_tools() {
};

Renderer_tools.prototype.render = function(entry, toolsArray) {
	var tools = null;

	var id = null;
	var content = null;
	var category = null;
	var type = null;

	if (entry != undefined && entry != null && entry.content != undefined && entry.content != null) {
		id = entry.id;
		content = JSON.parse(entry.content);
		category = content.category;
		type = content.type;
	}

	var xIndex = $.inArray('x', toolsArray);
	if (xIndex != -1) {
		tools = $('<a></a>').addClass('icon lax');
		tools.click(function() {
			__Caller.deleteUserContent(category, id);
		});
	} else {
		tools = $('<div></div>').addClass('entry_tools');
		var ul = $('<ul></ul>');
		for ( var i = 0; i < toolsArray.length; i++) {
			var tool = toolsArray[i];
			var li = $('<li></li>');
			var a = $('<a></a>').addClass('icon');

			if (tool == 'delete') {
				a.addClass('delete_black');
				a.click(function() {
					__Caller.deleteUserContent(category, id);
				});
			} else if (tool == 'edit') {
				a.addClass('edit_black');
				a.click(function() {
					// var container =
					// $(__Properties.categories[category].containerSelector);
					var parent = $(this).parents('.entry');
					parent.empty();

					if (type == 'raw') {
						__Forms.formRaw(category, parent, entry);
					} else if (type == 'simple') {
						__Forms.formSimple(category, parent, entry);
					} else if (type == 'simple_pic') {
						__Forms.formSimplePic(category, parent, entry);
					} else if (type == 'video') {
						__Forms.formVideo(category, parent, entry);
					}
				});
			} else if (tool == 'share') {
				a.addClass('share_black');
				// TODO share
			}

			li.append(a);
			ul.append(li);
		}

		tools.append(ul);
	}

	return tools;
};
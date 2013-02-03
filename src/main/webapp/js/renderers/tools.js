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

Renderer_tools.prototype.render = function(entry) {
	var tools = null;
	var ul = null;

	var linear = false;
	var toolsArray = [];

	var id = null;
	var content = null;
	var category = null;
	var type = null;

	if (entry != undefined && entry != null && entry.content != undefined && entry.content != null) {
		id = entry.id;
		//content = JSON.parse(entry.content);
		content = entry.content;
		category = content.category;
		type = content.type;
	}

	if (type == 'raw' || type == 'simple' || type == 'simple_desc' || type == 'simple_pic' || type == 'video'
			|| type == 'sys_simple') {
		tools = $('<div></div>').addClass('entry_tools');
		ul = $('<ul></ul>');
		tools.append(ul);
	} else {
		linear = true;
		tools = $('<span></span>');
	}

	if (__Current == 'manager') {
		if (category == 'studentinfo') {
			return tools;
		}
		toolsArray = __Properties.entries[content.type].tools;
	} else if (__Current == 'myportfolios') {
		if (__EditMode) {
			if (category == 'presentation' || type == 'contact') {
				toolsArray = [ 'eye' ];
			} else {
				toolsArray = [ 'eye', 'cherry' ];
			}
		} else {
			return tools;
		}
	}

	for ( var i = 0; i < toolsArray.length; i++) {
		var tool = toolsArray[i];
		var a = $('<a></a>').addClass('icon');

		if (tool == 'eye') {
			// a = $('<input/>').attr('type', 'checkbox');
			// a.addClass('eye');
			if (id != null && id != undefined) {
				a.attr('id', id);
			} else {
				a.attr('id', type);
			}

			//var myPortfolioCurrentContent = JSON.parse(__MyPortfoliosCurrent.content);
			var myPortfolioCurrentContent = __MyPortfoliosCurrent.content;
			if (id != null && id != undefined && $.inArray(id, myPortfolioCurrentContent['showUserGeneratedData']) > -1) {
				a.addClass('eye');
			} else if ($.inArray(type, myPortfolioCurrentContent['showStudentInfo']) > -1) {
				a.addClass('eye');
			} else {
				a.addClass('eye_black');
			}

			a.click(function() {
				var eye = $(this);
				var toggleEye = function() {
					if (eye.hasClass('eye_black')) {
						eye.removeClass('eye_black');
						eye.addClass('eye');
					} else if (eye.hasClass('eye')) {
						eye.removeClass('eye');
						eye.addClass('eye_black');
					}

					if (__EditMode_change != true) {
						__EditMode_change = true;
						__Renderer_Toolbar.enableSaveBtn();
					}
				};

				if (type == 'sys_simple' && eye.hasClass('eye_black')) {
					__Dialog = $('<div></div>').attr('id', 'dialog');
					__Dialog.append(__Properties.SYS_entry_dialog);
					$(__Dialog).dialog({
						modal : true,
						draggable : false,
						resizable : false,
						dialogClass : 'dialog notitle-dialog',
						buttons : [ {
							text : "Cancel",
							click : function() {
								$(this).dialog("close");
							}
						}, {
							text : "Continue",
							click : function() {
								$(this).dialog("close");
								toggleEye();
							}
						} ]
					});
				} else {
					toggleEye();
				}
			});
		} else if (tool == 'cherry' && __Utils.valid(id)) {
			a.attr('id', id);

			//var myPortfolioCurrentContent = JSON.parse(__MyPortfoliosCurrent.content);
			var myPortfolioCurrentContent = __MyPortfoliosCurrent.content;
			if ($.inArray(id, myPortfolioCurrentContent['highlightUserGeneratedData']) > -1) {
				a.addClass('cherry');
			} else {
				a.addClass('cherry_black');
			}

			a.click(function() {
				if ($(this).hasClass('cherry_black')) {
					$(this).removeClass('cherry_black');
					$(this).addClass('cherry');
				} else if ($(this).hasClass('cherry')) {
					$(this).removeClass('cherry');
					$(this).addClass('cherry_black');
				}

				if (__EditMode_change != true) {
					__EditMode_change = true;
					__Renderer_Toolbar.enableSaveBtn();
				}
			});
		} else if (tool == 'x') {
			a.addClass('lax');
			a.click(function() {
				__Caller.deleteUserContent(category, id);
			});
		} else if (tool == 'delete') {
			a.addClass('delete_black');
			a.click(function() {
				__Caller.deleteUserContent(category, id);
			});
		} else if (tool == 'edit') {
			a.addClass('edit_black');
			a.click(function() {
				var parent = $(this).parents('.entry');
				parent.empty();

				if (type == 'raw') {
					__Forms.formRaw(category, parent, entry);
				} else if (type == 'simple') {
					__Forms.formSimple(category, parent, entry);
				} else if (type == 'simple_desc') {
					__Forms.formSimpleDesc(category, parent, entry);
				} else if (type == 'simple_pic') {
					__Forms.formSimplePic(category, parent, entry);
				} else if (type == 'video') {
					__Forms.formVideo(category, parent, entry);
				}
			});
		} else if (tool == 'share') {
			a.addClass('share_black');
			// TODO share
		} else {
			a = null;
		}

		if (__Utils.valid(a)) {
			if (linear) {
				tools.append(a);
			} else {
				var li = $('<li></li>').append(a);
				ul.append(li);
			}
		}
	}

	return tools;
};

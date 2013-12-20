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
function Renderer_OverviewSection() {
};

Renderer_OverviewSection.prototype.render = function(category, sectionSelector, data) {
	var entriesArray = JSON.parse(data);

	if (__Current == 'myportfolios' && __EditMode == false) {
		//var mpc = JSON.parse(__MyPortfoliosCurrent.content);
		var mpc = __MyPortfoliosCurrent.content;
		entriesArray = __Utils.filterEntries(entriesArray, mpc['showUserGeneratedData'], 'id');

		if (entriesArray.length == 0) {
			$(sectionSelector).remove();
			return;
		}
	}

	$(sectionSelector + ' ul .overview_userdata').remove();
	var ul = $(sectionSelector + ' ul');

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
		}

		if (renderer != null) {
			var li = renderer.render(entry);
			ul.append(li);
		}
	}
};

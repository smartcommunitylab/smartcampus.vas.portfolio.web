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
function Renderer_TabMenu() {
};

Renderer_TabMenu.prototype.render = function(tabMenuSelector) {
	$(tabMenuSelector + ' ul').empty();
	var ul = $(tabMenuSelector + ' ul');
	var li;
	var a;

	// add new portfolio
	li = $('<li></li>');
	a = $('<a></a>').attr('id', 'tab_p_add').text('+');
	li.click(function() {
		__Forms.form('portfolio', $(this), null);
	});
	li.append(a);
	ul.append(li);

	for ( var i = 0; i < __MyPortfolios.length; i++) {
		if (i == 0 && (__MyPortfoliosCurrent == undefined || __MyPortfoliosCurrent == null)) {
			__MyPortfoliosCurrent = __MyPortfolios[i];
		}

		var id = __MyPortfolios[i]['id'];
		//var content = JSON.parse(__MyPortfolios[i].content);
		var content = __MyPortfolios[i].content;

		li = $('<li></li>');
		a = $('<a></a>').attr('id', 'tab_p_' + i).text(content['name']);

		if (__MyPortfoliosCurrent['id'] == id) {
			$(tabMenuSelector + ' ul li').removeClass('active');
			li.addClass('active');
		}

		a.click(function() {
			__MyPortfoliosCurrent = __MyPortfolios[($(this).attr('id')).replace('tab_p_', '')];
			$(tabMenuSelector + ' ul li').removeClass('active');
			$(this).parent().addClass('active');
			__Caller.refresh();
			__Renderer_Toolbar.render();
			__Utils.refreshUI();
		});
		li.append(a);

		// deletion
		var ax = $('<a></a>').addClass('icon lax').attr('id', 'tab_p_x_' + id);
		ax.click(function() {
			__Caller.deletePortfolio(($(this).attr('id')).replace('tab_p_x_', ''));
		});
		li.append(ax);

		ul.append(li);
	}
};

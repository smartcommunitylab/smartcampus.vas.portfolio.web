/**
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
var __TEST;
var __Properties;

var __Sec;

var __Main;
var __Utils;
var __Caller;
var __Renderer_Tools;
var __Forms;

var __Current;
var __MyPortfolios;
var __MyPortfoliosCurrent;
var __MyPortfoliosCurrentCherries = [];
var __MyPortfoliosCurrentCherriesDone = false;
var __Notes;

var __EditMode = false;
var __EditMode_change = false;

var __Profile;
var __StudentData;
var __IsStudent = false;

var __Dialog;

$(document).ready(function() {
	__Properties = new Properties();

	__Utils = new Utils();
	__Caller = new Caller();
	__Renderer_TabMenu = new Renderer_TabMenu();
	__Renderer_Toolbar = new Renderer_Toolbar();
	__Renderer_Tools = new Renderer_tools();
	__Forms = new Forms();
	__Main = new Main();

	__Sec = new Security();
	// var ok = __Sec.checkLogin();
	// if (ok) {
	__Main.init();
	// }

	__TEST = new Test();
});

function Main() {

	this.init = function() {
		$('#tab_menu').hide();
		$('#m_manager').addClass('active');

		// show tools on hover
		__Utils.refreshUI();

		__Caller.getProfile();
		__Utils.refreshUI();
		// __Main.go('profilenew');
	};

	this.go = function(page) {
		__Current = page;

		// top menu
		if (__Current != 'exams') {
			$('#top_menu ul li').removeClass('active');
			$('#m_' + __Current).addClass('active');
		}

		if (__Current != 'myportfolios') {
			$('#tab_menu').slideUp();
		}

		var callback = function() {
			if (__Current == 'manager') {
				__Renderer_Toolbar.render();
				__Caller.refresh();
				__Utils.refreshUI();
			} else if (__Current == 'myportfolios') {
				$('#tab_menu').slideDown();
				__Caller.getPortfolios('#tab_menu');
				if (__MyPortfolios.length > 0) {
					__Renderer_Toolbar.render();
					__Caller.refresh();
					__Utils.refreshUI();
				} else {
					__Current = 'myportfolios_empty';
					__Renderer_Toolbar.render();
					__Caller.load(__Current, '#main', null);
				}
			} else if (__Current == 'noticeboard') {
				__Renderer_Toolbar.render();
			} else if (__Current == 'notes') {
				__Renderer_Toolbar.render();
				__Caller.getUserData('#notes');
			} else if (__Current == 'exams') {
				// __Renderer_Toolbar.render();
				__Caller.getStudentExams('#exams');
			}
		};

		// snippet loader and executor
		if (__Current == 'exams') {
			callback();
		} else {
			__Caller.load(__Current, '#main', callback);
		}
	};

}

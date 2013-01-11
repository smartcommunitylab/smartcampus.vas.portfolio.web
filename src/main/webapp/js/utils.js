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
function Utils() {
};

Utils.prototype.updateTools = function() {
	if (__Current == 'manager') {
		/*
		 * show tools on hover
		 */
		// entries tools
		$('.entry').hover(function() {
			// $('.entry_tools', this).show(__Properties.showmsec);
			$('.entry_tools', this).addClass('show');
		}, function() {
			// $('.entry_tools', this).hide(__Properties.hidemsec);
			$('.entry_tools', this).removeClass('show');
		});

		// photo
		$('.overview_photo').hover(function() {
			$('.entry_tools', this).show(__Properties.showmsec);
		}, function() {
			$('.entry_tools', this).hide(__Properties.hidemsec);
		});

		// overview entries "x"
		$('.overview_sub ul li').hover(function() {
			$('a', this).css('display', 'inline-block');
		}, function() {
			$('a', this).hide(__Properties.hidemsec);
		});
	} else if (__Current == 'myportfolios') {
		$('.entry .entry_tools').addClass('show');
		$('.overview_photo .entry_tools').show();
		$('.overview_sub ul li a').css('display', 'inline-block');
	}
};

// Utils.prototype.actionTopMenu = function() {
// var lis = $('#top_menu ul li');
// for ( var i = 0; i < lis.length; i++) {
// var li = lis[i];
// var id = $(li).attr('id');
// if (li != undefined && li != null && id != undefined && id != null && id !=
// '') {
// $(li).click(function() {
// __Main.go(id.slice(2));
// });
// }
// }
// };

Utils.prototype.refreshUI = function() {
	$('.loader').remove();
	// this.actionTopMenu();
	this.updateTools();
	$('.oembed').oembed(null, {
		maxWidth : __Properties.entries['video'].maxWidth,
		maxHeight : __Properties.entries['video'].maxHeight
	});
};

Utils.prototype.checkSysEntry = function(data) {
	var entriesArray = JSON.parse(data);
	for ( var i = 0; i < entriesArray.length; i++) {
		var entry = entriesArray[i];
		var content = JSON.parse(entry.content);
		if (content['type'].indexOf('sys_') != -1) {
			return true;
		}
	}
	return false;
};

Utils.prototype.filterEntries = function(entriesArray, enabled, field) {
	var filteredEntries = [];
	for ( var i = 0; i < entriesArray.length; i++) {
		var entry = entriesArray[i];
		if ($.inArray(entry[field], enabled) != -1) {
			filteredEntries.push(entry);
		}
	}
	return filteredEntries;
};

Utils.prototype.cleanDuplicates = function(things) {
	var arr = {};

	for ( var i = 0; i < things.length; i++) {
		arr[things[i]['id']] = things[i];
	}

	things = new Array();
	for (key in arr) {
		things.push(arr[key]);
	}

	return things;
};

Utils.prototype.getEntriesByIds = function(entriesArray, idsArray) {
	var getArray = [];

	for ( var i = 0; i < idsArray.length; i++) {
		var id = idsArray[i];

		for ( var j = 0; j < entriesArray.length; j++) {
			var entry = entriesArray[j];
			if (entry['id'] == id) {
				getArray.push(entry);
				break;
			}
		}
	}

	return getArray;
};

Utils.prototype.valid = function(object) {
	return (object != undefined && object != null && object != '');
};

Utils.prototype.firstUppercase = function(string) {
	return string.charAt(0).toUpperCase() + string.slice(1);
};

Date.prototype.customFormat = function(formatString) {
	var YYYY, YY, MMMM, MMM, MM, M, DDDD, DDD, DD, D, hhh, hh, h, mm, m, ss, s, ampm, AMPM, dMod, th;
	var dateObject = this;
	YY = ((YYYY = dateObject.getFullYear()) + "").slice(-2);
	MM = (M = dateObject.getMonth() + 1) < 10 ? ('0' + M) : M;
	MMM = (MMMM = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
			"November", "December" ][M - 1]).substring(0, 3);
	DD = (D = dateObject.getDate()) < 10 ? ('0' + D) : D;
	DDD = (DDDD = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ][dateObject.getDay()])
			.substring(0, 3);
	th = (D >= 10 && D <= 20) ? 'th' : ((dMod = D % 10) == 1) ? 'st' : (dMod == 2) ? 'nd' : (dMod == 3) ? 'rd' : 'th';
	formatString = formatString.replace("#YYYY#", YYYY).replace("#YY#", YY).replace("#MMMM#", MMMM).replace("#MMM#",
			MMM).replace("#MM#", MM).replace("#M#", M).replace("#DDDD#", DDDD).replace("#DDD#", DDD)
			.replace("#DD#", DD).replace("#D#", D).replace("#th#", th);

	h = (hhh = dateObject.getHours());
	if (h == 0)
		h = 24;
	if (h > 12)
		h -= 12;
	hh = h < 10 ? ('0' + h) : h;
	AMPM = (ampm = hhh < 12 ? 'am' : 'pm').toUpperCase();
	mm = (m = dateObject.getMinutes()) < 10 ? ('0' + m) : m;
	ss = (s = dateObject.getSeconds()) < 10 ? ('0' + s) : s;
	return formatString.replace("#hhh#", hhh).replace("#hh#", hh).replace("#h#", h).replace("#mm#", mm).replace("#m#",
			m).replace("#ss#", ss).replace("#s#", s).replace("#ampm#", ampm).replace("#AMPM#", AMPM);
};

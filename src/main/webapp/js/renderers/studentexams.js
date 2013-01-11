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
function Renderer_StudentExams() {
};

Renderer_StudentExams.prototype.render = function(containerSelector, data) {
	// var container = $(containerSelector)find('table tbody');

	var fill = function() {
		var container = $(__Dialog).find('table tbody');
		container.empty();

		var json = JSON.parse(data);
		var content = JSON.parse(json.content);
		var examdata = content.examData;

		// Exam, Weight, Date, Result
		if (__Utils.valid(examdata) && __Utils.valid(examdata.length)) {
			for ( var i = 0; i < examdata.length; i++) {
				var tr = $('<tr></tr>');
				var exam = examdata[i];

				// name
				var td = $('<td></td>').addClass('exam_name');
				td.append(exam['name']);
				tr.append(td);

				// weight
				td = $('<td></td>');
				td.append(exam['weight']);
				tr.append(td);

				// result
				td = $('<td></td>');
				var resultString = exam['result'];
				if (resultString == '' || resultString == 0) {
					resultString = '-';
				}
				if (exam['lode'] == true) {
					resultString = resultString + 'L';
				}
				td.append(resultString);
				tr.append(td);

				// date
				td = $('<td></td>');
				var dateString = '-';
				if (exam['date'] > 0) {
					var date = new Date(exam['date']);
					dateString = date.customFormat('#DD#/#MM#/#YYYY#');
				}
				td.append(dateString);
				tr.append(td);

				container.append(tr);
			}
		}

		$(__Dialog).dialog({
			modal : true,
			draggable : false,
			resizable : false,
			minWidth : 760,
			dialogClass : 'dialog notitle-dialog',
			buttons : [ {
				text : "Close",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		});
	};

	// fill();
	
	__Dialog = $('<div></div>').attr('id', 'dialog-exams');
	__Dialog.attr('title', 'Exams');
	__Caller.load('exams', __Dialog, fill);
};

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
function Properties() {
	this.staticUrl = '/portfolio/';
	this.baseUrl = 'https://' + window.document.location.hostname + ':' + window.document.location.port + this.staticUrl;
	this.authUrl = 'https://ac.smartcampuslab.it/ac-service-provider-web-dev/ac/getToken?code=&redirect=';
	this.validationUrl = this.baseUrl + 'rest/validatetoken/';

	this.showmsec = 0;
	this.hidemsec = 0;

	this.studentInfos = [ 'cds', 'enrollmentYear', 'academicYear', 'supplementaryYears', 'cfu_cfuTotal', 'marksNumber',
			'marksAverage', 'gender', 'dateOfBirth', 'nation', 'address', 'email', 'phone', 'mobile' ];

	this.categories = {
		'education' : {
			containerSelector : '#section_education',
			choose : [ 'simple' ]
		},
		'presentation' : {
			containerSelector : '#section_presentation',
			choose : [ 'raw', 'video' ]
		},
		'professional' : {
			containerSelector : '#section_professional',
			choose : [ 'simple_desc' ]
		},
		'about' : {
			containerSelector : '#section_about',
			choose : [ 'simple_pic' ]
		},
		'cherryotc' : {
			containerSelector : '#section_cherryotc',
			choose : []
		}
	};

	this.entries = {
		'user_pic' : {
			tools : [ 'x' ]
		},
		'language' : {
			tools : [ 'x' ]
		},
		'skill' : {
			tools : [ 'x' ]
		},
		'contact' : {
			tools : [ 'x' ]
		},
		'raw' : {
			tools : [ 'edit', 'delete', 'share' ]
		},
		'simple' : {
			tools : [ 'edit', 'delete', 'share' ]
		},
		'simple_desc' : {
			tools : [ 'edit', 'delete', 'share' ]
		},
		'simple_pic' : {
			tools : [ 'edit', 'delete', 'share' ]
		},
		'video' : {
			tools : [ 'edit', 'delete', 'share' ],
			maxWidth : 390,
			maxHeight : 293
		},
		'sys_simple' : {
			tools : [ 'share' ]
		}
	};

	this.SYS_entry_dialog = 'Having enabled this entry, if you share this portfolio, your university marks will be visible to other users.';
};

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
function Renderer_Entry_raw() {
};

Renderer_Entry_raw.prototype.render = function(entry) {
	//var content = JSON.parse(entry.content);
	var content = entry.content;

	var div = $('<div></div>').addClass('entry');
	div.addClass('raw');

	var tools = __Renderer_Tools.render(entry);
	div.append(tools);

	var body = $('<div></div>').addClass('entry_body');
	body.html(content['content']);
	div.append(body);

	return div;
};

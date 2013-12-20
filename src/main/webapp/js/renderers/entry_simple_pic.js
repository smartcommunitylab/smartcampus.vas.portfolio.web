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
function Renderer_Entry_simple_pic() {
};

Renderer_Entry_simple_pic.prototype.render = function(entry) {
	//var content = JSON.parse(entry.content);
	var content = entry.content;

	var div = $('<div></div>').addClass('entry');
	div.addClass('simple_pic');

	var tools = __Renderer_Tools.render(entry);
	div.append(tools);

	var body = $('<div></div>').addClass('entry_body');
	var image = $('<img />');
	image.attr('alt', 'pic');
	image.attr('src', content['content']);
	body.append(image);

	var text = $('<div></div>').addClass('text');
	var title = $('<div></div>').addClass('title');
	title.text(content['title']);
	text.append(title);
	var subtitle = $('<div></div>').addClass('subtitle');
	subtitle.text(content['subtitle']);
	text.append(subtitle);
	body.append(text);

	div.append(body);
	return div;
};

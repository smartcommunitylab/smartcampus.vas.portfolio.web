<%--
Copyright 2012-2013 Trento RISE

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="lib/jquery/ui/jquery-ui-1.9.2.custom.min.css"
	rel="stylesheet" type="text/css">
<link href="css/main.css" rel="stylesheet" type="text/css">
<script>
	auth_token = '<%=request.getAttribute("token")%>';
</script>
<script type="text/javascript" src="lib/json2.js"></script>
<script type="text/javascript" src="lib/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript"
	src="lib/jquery/plugins/jquery.oembed.min.js"></script>
<script type="text/javascript"
	src="lib/jquery/ui/jquery-ui-1.9.2.custom.min.js"></script>
<!-- CUSTOM -->
<script type="text/javascript" src="js/renderers/entry_raw.js"></script>
<script type="text/javascript" src="js/renderers/entry_simple.js"></script>
<script type="text/javascript" src="js/renderers/entry_simple_desc.js"></script>
<script type="text/javascript" src="js/renderers/entry_simple_pic.js"></script>
<script type="text/javascript" src="js/renderers/entry_video.js"></script>
<script type="text/javascript" src="js/renderers/entry_sys_simple.js"></script>
<script type="text/javascript" src="js/renderers/entry_language.js"></script>
<script type="text/javascript" src="js/renderers/entry_skill.js"></script>
<script type="text/javascript" src="js/renderers/entry_contact.js"></script>
<script type="text/javascript" src="js/renderers/studentinfo.js"></script>
<script type="text/javascript" src="js/renderers/studentexams.js"></script>
<script type="text/javascript" src="js/renderers/notes.js"></script>
<script type="text/javascript" src="js/renderers/tools.js"></script>
<script type="text/javascript" src="js/renderers/section.js"></script>
<script type="text/javascript" src="js/renderers/overviewsection.js"></script>
<script type="text/javascript" src="js/renderers/overview.js"></script>
<script type="text/javascript" src="js/renderers/tabmenu.js"></script>
<script type="text/javascript" src="js/renderers/toolbar.js"></script>
<script type="text/javascript" src="js/properties.js"></script>
<script type="text/javascript" src="js/sec.js"></script>
<script type="text/javascript" src="js/utils.js"></script>
<script type="text/javascript" src="js/forms.js"></script>
<script type="text/javascript" src="js/caller.js"></script>
<script type="text/javascript" src="js/test.js"></script>
<script type="text/javascript" src="js/main.js"></script>
<title>MyCVs</title>
</head>
<body>
	<div id="top_menu">
		<ul>
			<li id="m_manager" onclick="__Main.go('manager');">MY DATA</li>
			<li id="m_myportfolios" onclick="__Main.go('myportfolios');">MY
				CVs</li>
			<li id="m_noticebard" onclick="__Main.go('noticeboard');">NOTICEBOARD</li>
			<li id="m_notes" onclick="__Main.go('notes');">NOTES</li>
			<li><a class="icon guide"></a> <a class="icon close"
				onclick="__Sec.logout();"></a></li>
		</ul>
	</div>
	<!-- TAB MENU: DO NOT DELETE THIS!!!!! -->
	<div id="tab_menu">
		<ul>
			<!-- <li><a href="#">First portfolio</a></li>
			<li><a href="#">Second portfolio</a></li>
			<li class="active"><a href="#">Third portfolio</a></li>
			<li><a href="#">+</a></li> -->
		</ul>
	</div>
	<div id="toolbar">
		<ul class="toolbar_tools">
			<!-- <li><a href="#"><span class="icon ib import"></span><span>Import
						external data</span></a></li>
			<li><a href="#"><span class="icon ib add_section"></span><span>Add
						section</span></a></li> -->
		</ul>
		<div class="toolbar_buttons"></div>
		<div class="toolbar_tags"></div>
	</div>
	<div id="main">
		<!-- <div id="main_left">
			<div id="overview">
				<div id="overview_card"></div>
				<div id="overview_personal"></div>
				<div id="overview_languages">
					<div class="overview_sub_header"></div>
					<ul></ul>
				</div>
				<div id="overview_skills">
					<div class="overview_sub_header"></div>
					<ul></ul>
				</div>
				<div id="overview_contacts">
					<div class="overview_sub_header"></div>
					<ul></ul>
				</div>
			</div>
			<div class="section" id="section_education"></div>
		</div>
		<div id="main_right">
			<div class="section" id="section_presentation"></div>
			<div class="section" id="section_professional"></div>
			<div class="section" id="section_about"></div>
		</div>
		<div class="clear"></div> -->
	</div>
	<div id="footer">
		<div class="footerbg"></div>
	</div>
</body>
</html>

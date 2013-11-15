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

<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="pm">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="lib/jquery/ui/jquery-ui-1.10.3.custom.min.css" rel="stylesheet" type="text/css">
<link href="css/main.css" rel="stylesheet" type="text/css">
<link href="css/animate.min.css" rel="stylesheet" type="text/css">

<script>
	auth_token = '<%=request.getAttribute("token")%>';
</script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.0/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.0/angular-resource.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.0/angular-sanitize.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.0/angular-animate.min.js"></script>

<script src="https://code.jquery.com/jquery-1.10.1.min.js"></script>
<script src="https://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="lib/jquery/plugins/jquery.oembed.min.js"></script>
<script type="text/javascript" src="lib/jquery/ui/jquery-ui-1.10.3.custom.min.js"></script>

<!-- CUSTOM -->
<script type="text/javascript" src="js/properties.js"></script>
<script type="text/javascript" src="js/utils.js"></script>
<script type="text/javascript" src="js/services.js"></script>
<title>MyCVs</title>
</head>
<body ng-controller="MainController">
	<div id="top_menu">
		<ul>
			<li id="m_manager" ng-class="{ 'active': isCurrentView('manager') }" ng-click="setCurrentView('manager')">MY DATA</li>
			<li id="m_myportfolios" ng-class="{ 'active': isCurrentView('myportfolios') }" ng-click="setCurrentView('myportfolios')">MY CVs</li>
			<li id="m_noticeboard" ng-class="{ 'active': isCurrentView('noticeboard') }" ng-click="setCurrentView('noticeboard')">NOTICEBOARD</li>
			<li id="m_notes" ng-class="{ 'active': isCurrentView('notes') }" ng-click="setCurrentView('notes')">NOTES</li>
			<li><a class="icon loader" ng-if="loading == true"></a></li>
			<li><a class="icon guide"></a> <a class="icon close" ng-click="logout()"></a></li>
		</ul>
	</div>
	<!-- TAB MENU: DO NOT DELETE THIS!!!!! -->
	<div id="tab_menu" ng-show="isCurrentView('myportfolios')">
		<ul>
			<li ng-click="formStart()">
				<a id="tab_p_add">+</a>
				<form class="form" ng-controller="FormsController" ng-if="newPortfolioName != null">
					<input class="input_newportfolioname" type="text" placeholder="New CV name" ng-model="newPortfolioName" />
					<button ng-click="formNewPortfolioSend()">OK</button>
					<button ng-click="formCancel(null, $event)">Cancel</button>
				</form>
			</li>
			<li ng-class="{ 'active': p.id == myPortfolioCurrent.id}" ng-repeat="p in myPortfolios" ng-click="setPortfolioCurrent($index)">
				<a id="tab_p_{{$index}}">{{p.content.name}}</a>
				<a id="tab_p_x_{{p.id}}" class="icon lax" ng-click="caller.deletePortfolio(p.id)"></a>
			</li>
		</ul>
	</div>
	<div id="toolbar">
		<ul class="toolbar_tools">
			<li ng-show="isCurrentView('myportfolios')">
				<form>
					<input id="rb_edit" type="radio" name="editmode" value="edit" ng-click="editMode = true" ng-checked="editMode == true" />
					<label for="rb_edit">Edit</label>
					<input id="rb_preview" type="radio" name="editmode" value="preview" ng-click="editMode = false" ng-checked="editMode == false"/>
					<label for="rb_preview">Preview</label>
				</form>
			</li>
			<li ng-show="isCurrentView('myportfolios')">
				<a id="download_cv_pdf" ng-click=""><span class="icon ib export"></span><span>PDF export</span></a>
			</li>
			<li ng-show="isCurrentView('notes')">
				<a ng-click="formStart()"><span class="icon ib edit"></span><span>Edit notes</span></a>
			</li>
		</ul>
		<div class="toolbar_buttons">
			<a class="btn save_btn" ng-if="somethingChanged" ng-click="btnSave()">Save</a>
		</div>
		<div class="toolbar_tags"></div>
	</div>
	<div id="main" ng-include="'./html/' + currentView + '.html'">
	</div>
	<div id="dialog-exams" title="Exams" ng-include="'./html/exams.html'"></div>
	<div id="footer">
		<div class="footerbg"></div>
	</div>
</body>
</html>

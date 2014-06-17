<%-- Copyright 2012-2013 Trento RISE Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. --%>

    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

        <!DOCTYPE html>
        <html ng-app="pm">

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
            <link href="css/main.css" rel="stylesheet" type="text/css">
            <link href="css/animate.min.css" rel="stylesheet" type="text/css">

            <script>
            auth_token = '<%=request.getAttribute("token")%>';
            </script>
            <title>MyCVs</title>
        </head>

        <body ng-controller="MainController">
            <div id="top_menu" class="row">
                <nav class="col-md-6 col-md-offset-3 navbar" role="navigation">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle pull-left" data-toggle="collapse" data-target="#top_menu_collapse">
                            <div class="icon-toggler">
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                            </div>
                            <span class="navbar-text" ng-if="!! currentViewName">{{currentViewName}}</span>
                        </button>
                    </div>
                    <div class="collapse navbar-collapse" id="top_menu_collapse">
                        <ul class="nav navbar-nav">
                            <li id="m_manager" ng-class="{ 'active': isCurrentView('manager') }" ng-click="setCurrentView('manager', ('menu_my-data' | i18n))">
                                <a>{{'menu_my-data' | i18n}}</a>
                            </li>
                            <li id="m_myportfolios" ng-class="{ 'active': isCurrentView('myportfolios') }" ng-click="setCurrentView('myportfolios', ('menu_my-cvs' | i18n))">
                                <a>{{'menu_my-cvs' | i18n}}</a>
                            </li>
                            <!-- <li id="m_noticeboard" ng-class="{ 'active': isCurrentView('noticeboard') }" ng-click="setCurrentView('noticeboard', ('menu_noticeboard' | i18n))">
                                <a>{{'menu_noticeboard' | i18n}}</a>
                            </li> -->
                            <li id="m_notes" ng-class="{ 'active': isCurrentView('notes') }" ng-click="setCurrentView('notes', ('menu_notes' | i18n))">
                                <a>{{'menu_notes' | i18n}}</a>
                            </li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li>
                                <span class="icon loader" ng-if="loading == true"></span>
                            </li>
                            <li>
                                <a class="glyphicon glyphicon-question-sign top_menu_help">
                                    <span>{{'menu_help' | i18n}}</span>
                                </a>
                            </li>
                            <li>
                                <a class="glyphicon glyphicon-remove top_menu_logout" ng-click="logout()">
                                    <span>{{'menu_logout' | i18n}}</span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
            <!-- TAB MENU: DO NOT DELETE THIS!!!!! -->
            <div id="tab_menu" class="row" ng-show="isCurrentView('myportfolios')">
                <nav class="col-md-6 col-md-offset-3">
                    <ul class="tabs">
                        <li ng-click="formStart()">
                            <a id="tab_p_add">
                                <span class="glyphicon glyphicon-plus"></span>
                            </a>
                        </li>
                    </ul>
                    <ul class="tabs tabs_cvs">
                        <li ng-class="{ 'active': p.id == myPortfolioCurrent.id}" ng-repeat="p in myPortfolios" ng-click="setPortfolioCurrent($index)">
                            <a id="tab_p_{{$index}}">{{p.name}}</a>
                            <a id="tab_p_x_{{p.id}}" class="remove" ng-click="setPortfolioToBeDeleted(p)">
                                <span class="glyphicon glyphicon-remove"></span>
                            </a>
                        </li>
                    </ul>
                    <div class="btn-group btn-group-cvs">
                        <button type="button" class="btn btn-default btn-lg dropdown-toggle" data-toggle="dropdown">
                            {{myPortfolioCurrent.content.name}}
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li ng-class="{ 'active': p.id == myPortfolioCurrent.id}" ng-repeat="p in myPortfolios" ng-click="setPortfolioCurrent($index)">
                                <div>
                                    <span id="tab_p_{{$index}}">{{p.content.name}}</span>
                                    <a id="tab_p_x_{{p.id}}" class="remove pull-right" ng-click="setPortfolioToBeDeleted(p)">
                                        <span class="glyphicon glyphicon-remove"></span>
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
            <div id="toolbar" class="row">
                <div class="col-md-6 col-md-offset-3">
                    <ul class="toolbar_tools list-unstyled list-inline">
                        <li ng-if="isCurrentView('myportfolios')">
                            <form>
                                <input id="rb_edit" type="radio" name="editmode" value="edit" ng-click="setEditMode(true)" ng-checked="editMode == true" />
                                <label for="rb_edit">{{'toolbar_edit' | i18n}}</label>
                                <input id="rb_preview" type="radio" name="editmode" value="preview" ng-click="setEditMode(false)" ng-checked="editMode == false" />
                                <label for="rb_preview">{{'toolbar_preview' | i18n}}</label>
                            </form>
                        </li>
                        <li ng-if="isCurrentView('myportfolios')">
                            <a id="download_cv_pdf" ng-click="caller.exportPortfolio(myPortfolioCurrent)">
                                <span class="icon ib export"></span>
                                <span>{{'toolbar_pdf-export' | i18n}}</span>
                            </a>
                        </li>
                        <li ng-if="isCurrentView('notes')">
                            <a ng-click="formStart()">
                                <span class="icon ib edit"></span>
                                <span>{{'toolbar_edit-notes' | i18n}}</span>
                            </a>
                        </li>
                    </ul>
                    <div class="toolbar_buttons">
                        <button class="btn btn-primary btn-save" ng-if="somethingChanged" ng-click="btnSave()">Save</button>
                    </div>
                    <div class="toolbar_tags"></div>
                </div>
            </div>

            <div id="main" class="row" ng-include="'./html/' + currentView + '.html'"></div>

            <div id="footer" class="row">
                <div class="col-md-3 col-md-offset-6">
                    <div class="footerbg">
                    </div>
                </div>
            </div>

            <div class="modal fade" id="examsModal" role="dialog" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body" ng-include="'./html/exams.html'"></div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">{{'btn_close' | i18n}}</button>
                        </div>
                    </div>
                </div>
            </div>

            <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.7/angular.min.js"></script>
            <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.7/angular-resource.min.js"></script>
            <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.7/angular-sanitize.min.js"></script>
            <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.7/angular-animate.min.js"></script>
            <script src="js/vendor/angularjs/localize.js"></script>
            <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
            <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
            <script src="js/vendor/jquery/plugins/jquery.oembed.min.js"></script>
            <script src="js/vendor/bootstrap.min.js"></script>
            <script src="js/vendor/Blob.js" type="text/javascript"></script>
            <script src="js/vendor/FileSaver.js" type="text/javascript"></script>

            <!-- CUSTOM -->
            <script type="text/javascript" src="js/properties.js"></script>
            <script type="text/javascript" src="js/utils.js"></script>
            <script type="text/javascript" src="js/services.js"></script>
        </body>

        </html>

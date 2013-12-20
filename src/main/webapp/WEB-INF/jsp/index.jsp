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
                <div class="col-md-6 col-md-offset-3">
                    <ul class="list-unstyled list-inline">
                        <li id="m_manager" ng-class="{ 'active': isCurrentView('manager') }" ng-click="setCurrentView('manager')">MY DATA</li>
                        <li id="m_myportfolios" ng-class="{ 'active': isCurrentView('myportfolios') }" ng-click="setCurrentView('myportfolios')">MY CVs</li>
                        <li id="m_noticeboard" ng-class="{ 'active': isCurrentView('noticeboard') }" ng-click="setCurrentView('noticeboard')">NOTICEBOARD</li>
                        <li id="m_notes" ng-class="{ 'active': isCurrentView('notes') }" ng-click="setCurrentView('notes')">NOTES</li>
                        <li>
                            <span class="icon loader" ng-if="loading == true"></span>
                        </li>
                        <li>
                            <span class="glyphicon glyphicon-question-sign"></span>
                            <span class="glyphicon glyphicon-remove" ng-click="logout()"></span>
                        </li>
                    </ul>
                </div>
            </div>
            <!-- TAB MENU: DO NOT DELETE THIS!!!!! -->
            <div id="tab_menu" class="row" ng-show="isCurrentView('myportfolios')">
                <div class="col-md-6 col-md-offset-3">
                    <ul>
                        <li ng-click="formStart()">
                            <a id="tab_p_add">
                                <span class="glyphicon glyphicon-plus"></span>
                            </a>
                            <form class="form-inline" ng-controller="FormsController" ng-if="newPortfolioName != null">
                                <input class="form-control" type="text" placeholder="New CV name" ng-model="newPortfolioName" />
                                <button class="btn btn-primary btn-form btn-sm" ng-click="formNewPortfolioSend()">OK</button>
                                <button class="btn btn-primary btn-form btn-sm" ng-click="formCancel(null, $event)">Cancel</button>
                            </form>
                        </li>
                        <li ng-class="{ 'active': p.id == myPortfolioCurrent.id}" ng-repeat="p in myPortfolios" ng-click="setPortfolioCurrent($index)">
                            <a id="tab_p_{{$index}}">{{p.content.name}}</a>
                            <!-- class="icon lax" -->
                            <a id="tab_p_x_{{p.id}}" class="remove" ng-click="caller.deletePortfolio(p.id)">
                                <span class="glyphicon glyphicon-remove"></span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <div id="toolbar" class="row">
                <div class="col-md-6 col-md-offset-3">
                    <ul class="toolbar_tools list-unstyled list-inline">
                        <li ng-if="isCurrentView('myportfolios')">
                            <form>
                                <input id="rb_edit" type="radio" name="editmode" value="edit" ng-click="editMode = true" ng-checked="editMode == true" />
                                <label for="rb_edit">Edit</label>
                                <input id="rb_preview" type="radio" name="editmode" value="preview" ng-click="editMode = false" ng-checked="editMode == false" />
                                <label for="rb_preview">Preview</label>
                            </form>
                        </li>
                        <li ng-if="isCurrentView('myportfolios')">
                            <a id="download_cv_pdf" target="_blank" ng-href="{{pdfbase64}}">
                                <span class="icon ib export"></span>
                                <span>PDF export</span>
                            </a>
                        </li>
                        <li ng-if="isCurrentView('notes')">
                            <a ng-click="formStart()">
                                <span class="icon ib edit"></span>
                                <span>Edit notes</span>
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
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.5/angular.min.js"></script>
            <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.5/angular-resource.min.js"></script>
            <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.5/angular-sanitize.min.js"></script>
            <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.5/angular-animate.min.js"></script>
            <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
            <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
            <script src="js/vendor/jquery/plugins/jquery.oembed.min.js"></script>
            <script src="js/vendor/bootstrap.min.js"></script>

            <!-- CUSTOM -->
            <script type="text/javascript" src="js/properties.js"></script>
            <script type="text/javascript" src="js/utils.js"></script>
            <script type="text/javascript" src="js/services.js"></script>
        </body>

        </html>

var pm = angular.module('pm', ['ngResource', 'ngSanitize', 'ngAnimate']);

pm.config(['$compileProvider',
    function($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|data):/);
    }
]);

function MainController($scope, $http, $resource, $location) {
    $scope.studentInfoDefaultPhoto = 'img/photo.png';

    $scope.currentView;
    $scope.loading = false;

    $scope.properties = new Properties();
    $scope.utils = new Utils();

    $scope.profile = {};
    $scope.studentInfo = {};
    $scope.studentPhoto = $scope.studentInfoDefaultPhoto;
    $scope.userProducedData = {};
    $scope.exams = [];
    $scope.notes = null;

    $scope.myPortfolios = null;
    $scope.myPortfolioCurrent = null;
    $scope.myPortfolioCurrentIndex = 0;
    $scope.myPortfolioCurrentCherries = [];

    $scope.activeForms = {};
    $scope.activeFormCategory = null;
    $scope.newPortfolioName = null;
    $scope.editMode = false;

    $scope.somethingChanged = false;
    $scope.showExams = false;

    $scope.setEditMode = function(em) {
        $scope.editMode = em;
    };

    $scope.setLoading = function(l) {
        if ($scope.loading != l) {
            $scope.loading = l;
        }
    };

    $scope.isStudent = function() {
        if ($scope.profile.hasOwnProperty('student')) {
            return $scope.profile.student;
        }
        return false;
    };

    $scope.setCurrentView = function(view) {
        $scope.somethingChanged = false;
        $scope.editMode = false;
        $scope.currentView = view;
    };

    $scope.isCurrentView = function(view) {
        return view == $scope.currentView ? true : false;
    };

    $scope.getToken = function() {
        return 'Bearer ' + auth_token;
    };

    $scope.logout = function() {
        window.document.location = "./logout";
    };

    $scope.getCherryEntries = function() {
        var allEntries = [];
        angular.forEach($scope.userProducedData, function(entries) {
            angular.forEach(entries, function(entry) {
                allEntries.push(entry);
            });
        });

        var cherryotcEntries = [];
        angular.forEach(allEntries, function(entry) {
            if ($scope.utils.valid($scope.myPortfolioCurrent) && $scope.myPortfolioCurrent.content.highlightUserGeneratedData.indexOf(entry.id) != -1) {
                cherryotcEntries.push(entry);
            }
        });


        return cherryotcEntries;
    };

    $scope.isCherry = function(id) {
        return $scope.myPortfolioCurrent.content.highlightUserGeneratedData.indexOf(id) != -1;
    };

    $scope.isStudentInfo = function(item) {
        if (item != undefined) {
            return $scope.properties.studentInfos.indexOf(item.category) != -1;
        }
        return false;
    };

    $scope.setPortfolioCurrent = function(index) {
        if ($scope.myPortfolios != null) {
            $scope.myPortfolioCurrentIndex = index;
            $scope.myPortfolioCurrent = angular.copy($scope.myPortfolios[index]);
            $scope.myPortfolioCurrentCherries = $scope.getCherryEntries();
        }
    };

    var authHeaders = {
        'Authorization': $scope.getToken()
    };

    var authHeadersPdf = {
        'Authorization': $scope.getToken(),
        'Content-Type': 'text/plain; charset=x-user-defined'
    };

    var Caller_Profile = $resource('rest/getprofile', {}, {
        'get': {
            method: 'GET',
            headers: authHeaders
        }
    });

    var Caller_StudentInfo = $resource('rest/smartcampus.services.esse3.StudentInfo', {}, {
        'get': {
            method: 'GET',
            headers: authHeaders
        }
    });

    var Caller_UserProducedData = $resource('rest/smartcampus.services.esse3.UserProducedData/:param', {}, {
        'query': {
            method: 'GET',
            headers: authHeaders,
            isArray: true
        },
        'save': {
            method: 'POST',
            headers: authHeaders
        },
        'update': {
            method: 'PUT',
            headers: authHeaders
        },
        'remove': {
            method: 'DELETE',
            headers: authHeaders
        }
    });

    var Caller_Portfolios = $resource('rest/smartcampus.services.esse3.Portfolio/:id', {}, {
        'query': {
            method: 'GET',
            headers: authHeaders,
            isArray: true
        },
        'save': {
            method: 'POST',
            headers: authHeaders
        },
        'update': {
            method: 'PUT',
            headers: authHeaders
        },
        'remove': {
            method: 'DELETE',
            headers: authHeaders
        }
    });

    var Caller_Notes = $resource('rest/eu.trentorise.smartcampus.portfolio.models.UserData/:id', {}, {
        'query': {
            method: 'GET',
            headers: authHeaders,
            isArray: true
        },
        'update': {
            method: 'PUT',
            headers: authHeaders
        }
    });

    var Caller_Exams = $resource('rest/smartcampus.services.esse3.StudentExams', {}, {
        'get': {
            method: 'GET',
            headers: authHeaders
        }
    });

    var Caller_Exporter = $resource('rest/generatecv/:portfolioId/pdf/true', {}, {
        'get': {
            method: 'GET',
            headers: authHeadersPdf,
            responseType: 'text'
        }
    });
    // req.overrideMimeType('text/plain; charset=x-user-defined');

    // CALLS
    $scope.caller = {};

    $scope.caller.getProfile = function() {
        $scope.setLoading(true);
        Caller_Profile.get({}, function(value, responseHeaders) {
            $scope.profile = value;
            $scope.setLoading(false);
        });
    };

    $scope.caller.getStudentInfo = function() {
        $scope.setLoading(true);
        Caller_StudentInfo.get({}, function(value, responseHeaders) {
            if (value.hasOwnProperty('content') && value.content.hasOwnProperty('studentData')) {
                $scope.studentInfo = value.content.studentData;
                if ($scope.studentInfo.photo != undefined && $scope.studentInfo.photo != null) {
                    $scope.studentPhoto = $scope.studentInfo.photo;
                }
            }
            $scope.setLoading(false);
        });
    };

    $scope.caller.getUserProducedData = function(category) {
        $scope.setLoading(true);
        Caller_UserProducedData.query({
            'param': category
        }, function(value, responseHeaders) {
            var contentsArray = [];
            angular.forEach(value, function(entry) {
                var content = entry.content;
                content.id = entry.id;

                // prevents errors for wrong previous savings
                if (!$scope.utils.valid(content.type)) {
                    content.type = content.category;
                }

                contentsArray.push(content);
            });
            $scope.userProducedData[category] = contentsArray;
            $scope.setLoading(false);
        });
    };

    $scope.caller.saveUserProducedData = function(entry) {
        $scope.setLoading(true);
        Caller_UserProducedData.save({}, entry, function(value, responseHeaders) {
            $scope.caller.getUserProducedData(entry.category);
            $scope.formCancel();
            $scope.setLoading(false);
        });
    };

    $scope.caller.updateUserProducedData = function(id, entry) {
        $scope.setLoading(true);
        Caller_UserProducedData.update({
            'param': id
        }, entry, function(value, responseHeaders) {
            $scope.caller.getUserProducedData(entry.category);
            $scope.formCancel();
            $scope.setLoading(false);
        });
    };

    $scope.caller.deleteUserProducedData = function(id, category) {
        $scope.setLoading(true);
        Caller_UserProducedData.remove({
            'param': id
        }, function(value, responseHeaders) {
            $scope.caller.getUserProducedData(category);
            $scope.setLoading(false);
        });
    };

    $scope.caller.getPortfolios = function() {
        $scope.setLoading(true);
        Caller_Portfolios.query({}, function(value, responseHeaders) {
            $scope.myPortfolios = value;
            $scope.setPortfolioCurrent($scope.myPortfolioCurrentIndex);
            $scope.setLoading(false);
        });
    };

    $scope.caller.savePortfolio = function(portfolio) {
        $scope.setLoading(true);
        Caller_Portfolios.save({}, portfolio, function(value, responseHeaders) {
            $scope.caller.getPortfolios();
            $scope.formCancel();
            $scope.setLoading(false);
        });
    };

    $scope.caller.updatePortfolio = function(portfolio) {
        $scope.setLoading(true);
        Caller_Portfolios.update({
            'id': portfolio.id
        }, portfolio.content, function(value, responseHeaders) {
            $scope.somethingChanged = false;
            $scope.editMode = false;
            $scope.caller.getPortfolios();
            $scope.setLoading(false);
        });
    };

    $scope.caller.deletePortfolio = function(id) {
        $scope.setLoading(true);
        Caller_Portfolios.remove({
            'id': id
        }, function(value, responseHeaders) {
            $scope.myPortfolioCurrentIndex = 0;
            $scope.caller.getPortfolios();
            $scope.setLoading(false);
        });
    };

    $scope.caller.getNotes = function() {
        $scope.setLoading(true);
        Caller_Notes.query({}, function(value, responseHeaders) {
            $scope.notes = value[0];
            $scope.setLoading(false);
        });
    };

    $scope.caller.updateNotes = function(id, data) {
        $scope.setLoading(true);
        Caller_Notes.update({
            'id': id
        }, data, function(value, responseHeaders) {
            $scope.editMode = false;
            $scope.caller.getNotes();
            $scope.formCancel();
            $scope.setLoading(false);
        });
    };

    $scope.caller.getExams = function() {
        $scope.setLoading(true);
        Caller_Exams.get({}, function(value, responseHeaders) {
            $scope.exams = value.content.examData;
            $scope.showExams = true;
            $('#examsModal').modal();
            $scope.setLoading(false);
        });
    };

    $scope.caller.exportPortfolio = function(portfolioId) {
        $scope.setLoading(true);

        if ( !! portfolioId) {
            $http({
                method: 'GET',
                url: 'rest/generatecv/' + portfolioId + '/pdf/true',
                headers: {
                    'Authorization': $scope.getToken(),
                    'Content-Type': 'text/plain; charset=x-user-defined'
                }
            }).
            success(function(data, status, headers, config) {
                $scope.setLoading(false);
                var encoded = encodeURIComponent(data);
                $scope.pdfbase64 = 'data:application/pdf;base64,' + encoded;
                // $scope.$apply(function() {
                $('download_cv_pdf').attr('href', $scope.pdfbase64);
                // });
            }).
            error(function(data, status, headers, config) {
                $scope.setLoading(false);
            });
        }

        // Caller_Exporter.get({
        //     'portfolioId': portfolioId
        // }, function(value, responseHeaders) {
        //     $scope.setLoading(false);
        //     var encoded = encodeURIComponent(value);
        //     var newpath = 'data:application/pdf;base64,' + encoded;
        //     $location.path(newpath);
        // });
    };

    // FORMS HELPERS
    $scope.formStart = function(category) {
        if ($scope.isCurrentView('manager') && $scope.utils.valid(category)) {
            $scope.activeFormCategory = category;

            $scope.activeForms[category] = {
                'id': 'new',
                'category': category,
                'type': '',
                'title': '',
                'subtitle': '',
                'content': ''
            };

            if ($scope.properties.categories[category] != null && $scope.properties.categories[category].choose != null && $scope.properties.categories[category].choose.length == 1) {
                $scope.activeForms[category]['type'] = $scope.properties.categories[category].choose[0];
            }
        } else if ($scope.isCurrentView('myportfolios')) {
            $scope.newPortfolioName = '';
        } else if ($scope.isCurrentView('notes')) {
            $scope.editMode = true;
            $scope.somethingChanged = true;
        }
    };

    $scope.formCancel = function(category, $event) {
        if ($scope.isCurrentView('manager') && $scope.utils.valid(category)) {
            $scope.activeForms[category] = {};
            $scope.activeFormCategory = null;
        } else if ($scope.isCurrentView('myportfolios')) {
            $scope.newPortfolioName = null;
            if ($scope.utils.valid($event)) {
                $event.stopImmediatePropagation();
            }
        } else if ($scope.isCurrentView('notes')) {
            $scope.editMode = false;
            $scope.somethingChanged = false;
        }
    };

    $scope.isShow = function(item) {
        var show;
        if ($scope.isStudentInfo(item)) {
            // studentInfo
            show = $scope.myPortfolioCurrent.content.showStudentInfo.indexOf(item.category) != -1;
        } else {
            // userProducedData
            show = $scope.myPortfolioCurrent.content.showUserGeneratedData.indexOf(item.id) != -1;
        }
        return show;
    };

    $scope.showEntry = function(item) {
        if ($scope.isCurrentView('manager')) {
            if (item == null || item.id == null) {
                return false;
            } else if ($scope.activeForms[item.category] == null || $scope.activeForms[item.category].id == null) {
                return true;
            } else {
                return ($scope.activeForms[item.category].id != item.id && item.id != null);
            }
        } else if ($scope.isCurrentView('myportfolios')) {
            if ($scope.editMode) {
                // edit mode
                return true;
            } else if ($scope.myPortfolioCurrent != null && $scope.myPortfolioCurrent.hasOwnProperty('content')) {
                return $scope.isShow(item);
            }
        }
    };

    $scope.showEntryForm = function(item) {
        if (item != null && ($scope.activeForms[item.category] == null || $scope.activeForms[item.category].id == null || $scope.activeFormCategory == null)) {
            return false;
        }

        return ((item != null && $scope.activeForms[item.category].id == item.id) || (item == null && $scope.activeForms[$scope.activeFormCategory] != null && $scope.activeForms[$scope.activeFormCategory].id == 'new'));
    };

    $scope.showSection = function(category) {
        if (category == 'cherryotc') {
            if ($scope.editMode == true) {
                return false;
            } else {
                return $scope.myPortfolioCurrent.content.highlightUserGeneratedData.length > 0;
            }
        } else {
            if ($scope.editMode == true) {
                return true;
            } else {
                var categories = [];
                if (angular.isArray(category)) {
                    categories = category;
                } else {
                    categories = [category];
                }

                for (var c = 0; c < categories.length; c++) {
                    var cat = categories[c];
                    if ($scope.isStudentInfo({
                        'category': cat
                    })) {
                        // studentInfo category
                        if ($scope.isShow({
                            'category': cat
                        })) {
                            return true;
                        }
                    } else {
                        if ( !! $scope.userProducedData[cat]) {
                            for (var i = 0; i < $scope.userProducedData[cat].length; i++) {
                                var entry = $scope.userProducedData[cat][i];
                                if ($scope.isShow(entry)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    // TOOLS
    $scope.getTools = function(item) {
        if (item == null) {
            return [];
        }

        if ($scope.isCurrentView('myportfolios') && $scope.editMode == true) {
            if ($scope.properties.studentInfos.indexOf(item.category) != -1) {
                return $scope.properties.toolsDefaultStudentInfos;
            } else if ($scope.properties.categories.hasOwnProperty(item.category) && $scope.properties.categories[item.category].hasOwnProperty('tools')) {
                return $scope.properties.categories[item.category].tools;
            } else {
                return $scope.properties.toolsDefaultCategories;
            }
        } else if ($scope.isCurrentView('manager') && $scope.properties.entries.hasOwnProperty(item.type) && $scope.properties.entries[item.type].hasOwnProperty('tools')) {
            return $scope.properties.entries[item.type].tools;
        } else {
            return [];
        }
    };

    // 'edit_black': tool == 'edit',
    // 'delete_black': tool == 'delete',
    $scope.getToolsClasses = function(tool, item) {
        var toolsClasses = {
            'icon lax': tool == 'lax',
            'glyphicon glyphicon-pencil': tool == 'edit',
            'glyphicon glyphicon-trash': tool == 'delete',
            'icon share_black': tool == 'share',
            'icon cherry': (tool == 'cherry' && $scope.isCherry(item.id)),
            'icon cherry_black': (tool == 'cherry' && !$scope.isCherry(item.id)),
            'icon eye': (tool == 'eye' && $scope.isShow(item)),
            'icon eye_black': (tool == 'eye' && !$scope.isShow(item))
        };

        return toolsClasses;
    };


    $scope.useTool_item;
    $scope.uniModalContinue = function() {
        $scope.myPortfolioCurrent.content.showUserGeneratedData.push($scope.useTool_item.id);
        $scope.somethingChanged = true;
        $('#uniModal').modal('hide');
    }

    $scope.useTool = function(tool, item) {
        $scope.useTool_item = item;

        if (tool == 'edit') {
            $scope.activeFormCategory = item.category;
            $scope.activeForms[item.category] = item;
        } else if (tool == 'delete' || tool == 'x' || tool == 'lax') {
            $scope.caller.deleteUserProducedData(item.id, item.category);
        } else if (tool == 'share') {
            // TODO
        } else if (tool == 'eye') {
            if ($scope.isStudentInfo(item)) {
                // studentInfos
                if ($scope.isShow(item)) {
                    $scope.myPortfolioCurrent.content.showStudentInfo.splice($scope.myPortfolioCurrent.content.showStudentInfo.indexOf(item.category), 1);
                } else {
                    $scope.myPortfolioCurrent.content.showStudentInfo.push(item.category);
                }
                $scope.somethingChanged = true;
            } else {
                // userProducedData
                if ($scope.isShow(item)) {
                    $scope.myPortfolioCurrent.content.showUserGeneratedData.splice($scope.myPortfolioCurrent.content.showUserGeneratedData.indexOf(item.id), 1);
                    $scope.somethingChanged = true;
                } else {
                    if (item.type == 'sys_simple') {
                        $('#uniModal').modal('show');
                    } else {
                        $scope.myPortfolioCurrent.content.showUserGeneratedData.push(item.id);
                        $scope.somethingChanged = true;
                    }
                }
            }
        } else if (tool == 'cherry') {
            if ($scope.isCherry(item.id)) {
                $scope.myPortfolioCurrent.content.highlightUserGeneratedData.splice($scope.myPortfolioCurrent.content.highlightUserGeneratedData.indexOf(item.id), 1);
            } else {
                $scope.myPortfolioCurrent.content.highlightUserGeneratedData.push(item.id);
            }
            $scope.myPortfolioCurrentCherries = $scope.getCherryEntries();
            $scope.somethingChanged = true;
        }
    };

    $scope.btnSave = function() {
        if ($scope.isCurrentView('myportfolios')) {
            $scope.caller.updatePortfolio($scope.myPortfolioCurrent);
        } else if ($scope.isCurrentView('notes')) {
            $scope.caller.updateNotes($scope.notes.id, $scope.notes);
        }
    }

    $scope.$watch('currentView', function(newValue, oldValue, scope) {
        $scope.somethingChanged = false;
        $scope.setPortfolioCurrent(0);

        if (newValue == 'manager') {
            $scope.caller.getProfile();
            $scope.caller.getStudentInfo();

            var userProducedDataCategories = ['language', 'skill', 'contact', 'education', 'presentation', 'professional', 'about'];
            angular.forEach(userProducedDataCategories, function(category) {
                $scope.caller.getUserProducedData(category);
            });
        } else if (newValue == 'myportfolios') {
            $scope.caller.getPortfolios();
        } else if (newValue == 'notes') {
            $scope.caller.getNotes();
        }
    });

    $scope.$watch('myPortfolioCurrent', function(newValue, oldValue, scope) {
        if ( !! newValue) {
            $scope.caller.exportPortfolio(newValue.id);
        }
    });

    // LET'S DO IT!
    $scope.setCurrentView('manager');
}

function FormsController($scope, $resource) {
    $scope.formSend = function(category) {
        var entry = $scope.activeForms[category];
        var id = entry.id;
        delete entry.id;

        if (!$scope.utils.valid(entry.type)) {
            entry.type = category;
        }

        if (id == 'new') {
            $scope.caller.saveUserProducedData(entry);
        } else {
            $scope.caller.updateUserProducedData(id, entry);
        }
    };

    $scope.formNewPortfolioSend = function() {
        if ($scope.utils.valid($scope.newPortfolioName)) {
            $scope.caller.savePortfolio({
                'name': $scope.newPortfolioName
            });
        }
    };
}

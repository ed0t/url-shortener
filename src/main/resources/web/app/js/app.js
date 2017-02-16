var app = angular.module('UrlShortener', [
    'ui.bootstrap',
    'ui.router',
    'ngclipboard'
]);

app.config(["$stateProvider", "$urlRouterProvider", "$locationProvider", "$httpProvider", function ($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {
    "ngInject";

    $httpProvider.defaults.withCredentials = true;
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

    $stateProvider
        .state('ui', {
            url: '/',
            templateUrl: 'app/views/home.tpl.html',
            controller: 'HomeController'
        })
        .state('redirect', {
            url: "/:id",
            controller: 'RedirectController'
        });

    $urlRouterProvider.otherwise('/');

}]);


app.controller('HomeController', ["$scope", "$http", function ($scope, $http) {
    "ngInject";

    $scope.alerts = [];
    $scope.url = "";
    $scope.shortenedUrl = null;

    var baseUrl = "http://127.0.0.1:8081";
    var path = "/api/urls";
    $scope.submitForm = function () {
        return $http.post(baseUrl + path, {url: $scope.url}).then(unwrapData, handleError);
    };

    $scope.clearUrl = function () {
        $scope.shortenedUrl = null;
        $scope.url = "";
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };

    function unwrapData(resp) {
        if (resp.status == 201) {
            $scope.shortenedUrl = resp.data;
            return resp.data;
        } else {
            throw new Error('Invalid response from ' + resp.config.url + ': ' + resp.status + ' ' + resp.statusText);
        }
    }

    function handleError(resp) {
        switch (resp.status) {
            case -1 :
                $scope.alerts.push({type: 'danger', msg: 'Impossible to connect to server.'});
                break;
            case 400 :
                $scope.alerts.push({type: 'danger', msg: resp.data});
        }
    }


}]);

app.controller('RedirectController', ["$scope", "$state", "$stateParams", "$http", "$window", function ($scope, $state, $stateParams, $http, $window) {
    "ngInject";

    var id = $stateParams.id;
    var baseUrl = "http://127.0.0.1:8081";
    var path = "/api/urls";

    $http.get(baseUrl + path + "/" + id).then(unwrapData, handleError);

    function unwrapData(resp) {
        if (resp.status == 200) {
            $window.location.href = resp.data;
        } else {
            $state.go("ui");
        }
    }

    function handleError(resp) {
        $state.go("ui");
    }

}]);



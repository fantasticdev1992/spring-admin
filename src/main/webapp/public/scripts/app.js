'use strict';

angular.module('registry', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute',
  'registry.services'
])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
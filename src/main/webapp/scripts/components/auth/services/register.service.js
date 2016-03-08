'use strict';

angular.module('modificandouserApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



'use strict';

angular.module('modificandouserApp')
    .factory('Trabajador', function ($resource, DateUtils) {
        return $resource('api/trabajadors/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

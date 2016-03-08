'use strict';

angular.module('modificandouserApp')
    .factory('Proceso', function ($resource, DateUtils) {
        return $resource('api/procesos/:id', {}, {
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

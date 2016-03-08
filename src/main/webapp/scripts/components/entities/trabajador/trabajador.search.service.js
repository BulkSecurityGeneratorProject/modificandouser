'use strict';

angular.module('modificandouserApp')
    .factory('TrabajadorSearch', function ($resource) {
        return $resource('api/_search/trabajadors/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

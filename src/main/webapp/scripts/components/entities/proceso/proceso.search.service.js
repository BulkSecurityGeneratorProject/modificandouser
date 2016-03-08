'use strict';

angular.module('modificandouserApp')
    .factory('ProcesoSearch', function ($resource) {
        return $resource('api/_search/procesos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

'use strict';

angular.module('modificandouserApp')
    .factory('Pedido', function ($resource, DateUtils) {
        return $resource('api/pedidos/:id', {}, {
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

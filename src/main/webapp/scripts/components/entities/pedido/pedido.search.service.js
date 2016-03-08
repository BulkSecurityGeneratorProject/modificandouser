'use strict';

angular.module('modificandouserApp')
    .factory('PedidoSearch', function ($resource) {
        return $resource('api/_search/pedidos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

'use strict';

angular.module('modificandouserApp')
    .controller('PedidoDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Pedido, Proceso, Trabajador, User) {
        $scope.pedido = entity;
        $scope.load = function (id) {
            Pedido.get({id: id}, function(result) {
                $scope.pedido = result;
            });
        };
        var unsubscribe = $rootScope.$on('modificandouserApp:pedidoUpdate', function(event, result) {
            $scope.pedido = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });

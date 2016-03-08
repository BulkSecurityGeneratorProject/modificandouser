'use strict';

angular.module('modificandouserApp')
	.controller('PedidoDeleteController', function($scope, $uibModalInstance, entity, Pedido) {

        $scope.pedido = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Pedido.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });

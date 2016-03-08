'use strict';

angular.module('modificandouserApp')
	.controller('TrabajadorDeleteController', function($scope, $uibModalInstance, entity, Trabajador) {

        $scope.trabajador = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Trabajador.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });

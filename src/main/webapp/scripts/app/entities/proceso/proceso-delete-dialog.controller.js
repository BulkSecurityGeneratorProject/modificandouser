'use strict';

angular.module('modificandouserApp')
	.controller('ProcesoDeleteController', function($scope, $uibModalInstance, entity, Proceso) {

        $scope.proceso = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Proceso.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });

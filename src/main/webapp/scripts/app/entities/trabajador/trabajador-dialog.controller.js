'use strict';

angular.module('modificandouserApp').controller('TrabajadorDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Trabajador', 'Proceso', 'Pedido',
        function($scope, $stateParams, $uibModalInstance, entity, Trabajador, Proceso, Pedido) {

        $scope.trabajador = entity;
        $scope.procesos = Proceso.query();
        $scope.pedidos = Pedido.query();
        $scope.load = function(id) {
            Trabajador.get({id : id}, function(result) {
                $scope.trabajador = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('modificandouserApp:trabajadorUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.trabajador.id != null) {
                Trabajador.update($scope.trabajador, onSaveSuccess, onSaveError);
            } else {
                Trabajador.save($scope.trabajador, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);

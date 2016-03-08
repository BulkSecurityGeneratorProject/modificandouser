'use strict';

angular.module('modificandouserApp').controller('ProcesoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Proceso', 'Pedido', 'Trabajador',
        function($scope, $stateParams, $uibModalInstance, entity, Proceso, Pedido, Trabajador) {

        $scope.proceso = entity;
        $scope.pedidos = Pedido.query();
        $scope.trabajadors = Trabajador.query();
        $scope.load = function(id) {
            Proceso.get({id : id}, function(result) {
                $scope.proceso = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('modificandouserApp:procesoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.proceso.id != null) {
                Proceso.update($scope.proceso, onSaveSuccess, onSaveError);
            } else {
                Proceso.save($scope.proceso, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);

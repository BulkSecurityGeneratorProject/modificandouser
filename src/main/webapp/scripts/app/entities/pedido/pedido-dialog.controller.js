'use strict';

angular.module('modificandouserApp').controller('PedidoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Pedido', 'Proceso', 'Trabajador', 'User',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Pedido, Proceso, Trabajador, User) {

        $scope.pedido = entity;
        $scope.procesos = Proceso.query();
        $scope.trabajadors = Trabajador.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Pedido.get({id : id}, function(result) {
                $scope.pedido = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('modificandouserApp:pedidoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.pedido.id != null) {
                Pedido.update($scope.pedido, onSaveSuccess, onSaveError);
            } else {
                Pedido.save($scope.pedido, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
}]);

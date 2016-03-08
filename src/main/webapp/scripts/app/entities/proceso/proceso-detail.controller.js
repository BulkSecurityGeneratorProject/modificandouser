'use strict';

angular.module('modificandouserApp')
    .controller('ProcesoDetailController', function ($scope, $rootScope, $stateParams, entity, Proceso, Pedido, Trabajador) {
        $scope.proceso = entity;
        $scope.load = function (id) {
            Proceso.get({id: id}, function(result) {
                $scope.proceso = result;
            });
        };
        var unsubscribe = $rootScope.$on('modificandouserApp:procesoUpdate', function(event, result) {
            $scope.proceso = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

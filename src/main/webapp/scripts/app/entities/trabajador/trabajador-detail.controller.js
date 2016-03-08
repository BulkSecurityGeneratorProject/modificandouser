'use strict';

angular.module('modificandouserApp')
    .controller('TrabajadorDetailController', function ($scope, $rootScope, $stateParams, entity, Trabajador, Proceso, Pedido) {
        $scope.trabajador = entity;
        $scope.load = function (id) {
            Trabajador.get({id: id}, function(result) {
                $scope.trabajador = result;
            });
        };
        var unsubscribe = $rootScope.$on('modificandouserApp:trabajadorUpdate', function(event, result) {
            $scope.trabajador = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

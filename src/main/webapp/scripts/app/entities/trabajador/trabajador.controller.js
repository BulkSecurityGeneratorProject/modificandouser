'use strict';

angular.module('modificandouserApp')
    .controller('TrabajadorController', function ($scope, $state, Trabajador, TrabajadorSearch) {

        $scope.trabajadors = [];
        $scope.loadAll = function() {
            Trabajador.query(function(result) {
               $scope.trabajadors = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            TrabajadorSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.trabajadors = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.trabajador = {
                nombre: null,
                email: null,
                dni: null,
                numeroTelf: null,
                id: null
            };
        };
    });

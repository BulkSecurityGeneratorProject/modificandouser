'use strict';

angular.module('modificandouserApp')
    .controller('ProcesoController', function ($scope, $state, Proceso, ProcesoSearch) {

        $scope.procesos = [];
        $scope.loadAll = function() {
            Proceso.query(function(result) {
               $scope.procesos = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ProcesoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.procesos = result;
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
            $scope.proceso = {
                nombre: null,
                id: null
            };
        };
    });

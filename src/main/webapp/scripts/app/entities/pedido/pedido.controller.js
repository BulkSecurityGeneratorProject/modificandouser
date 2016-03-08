'use strict';

angular.module('modificandouserApp')
    .controller('PedidoController', function ($scope, $state, DataUtils, Pedido, PedidoSearch) {

        $scope.pedidos = [];
        $scope.loadAll = function() {
            Pedido.query(function(result) {
               $scope.pedidos = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PedidoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.pedidos = result;
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
            $scope.pedido = {
                descripcion: null,
                costeSinIva: null,
                costeTotal: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });

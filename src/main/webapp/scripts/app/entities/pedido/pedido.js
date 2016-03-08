'use strict';

angular.module('modificandouserApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pedido', {
                parent: 'entity',
                url: '/pedidos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modificandouserApp.pedido.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedido/pedidos.html',
                        controller: 'PedidoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedido');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pedido.detail', {
                parent: 'entity',
                url: '/pedido/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modificandouserApp.pedido.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedido/pedido-detail.html',
                        controller: 'PedidoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedido');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pedido', function($stateParams, Pedido) {
                        return Pedido.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pedido.new', {
                parent: 'pedido',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog.html',
                        controller: 'PedidoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    descripcion: null,
                                    costeSinIva: null,
                                    costeTotal: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pedido', null, { reload: true });
                    }, function() {
                        $state.go('pedido');
                    })
                }]
            })
            .state('pedido.edit', {
                parent: 'pedido',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog.html',
                        controller: 'PedidoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Pedido', function(Pedido) {
                                return Pedido.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pedido', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('pedido.delete', {
                parent: 'pedido',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-delete-dialog.html',
                        controller: 'PedidoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Pedido', function(Pedido) {
                                return Pedido.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pedido', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

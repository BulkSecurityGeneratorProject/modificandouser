'use strict';

angular.module('modificandouserApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('trabajador', {
                parent: 'entity',
                url: '/trabajadors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modificandouserApp.trabajador.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/trabajador/trabajadors.html',
                        controller: 'TrabajadorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('trabajador');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('trabajador.detail', {
                parent: 'entity',
                url: '/trabajador/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modificandouserApp.trabajador.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/trabajador/trabajador-detail.html',
                        controller: 'TrabajadorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('trabajador');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Trabajador', function($stateParams, Trabajador) {
                        return Trabajador.get({id : $stateParams.id});
                    }]
                }
            })
            .state('trabajador.new', {
                parent: 'trabajador',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/trabajador/trabajador-dialog.html',
                        controller: 'TrabajadorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nombre: null,
                                    email: null,
                                    dni: null,
                                    numeroTelf: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('trabajador', null, { reload: true });
                    }, function() {
                        $state.go('trabajador');
                    })
                }]
            })
            .state('trabajador.edit', {
                parent: 'trabajador',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/trabajador/trabajador-dialog.html',
                        controller: 'TrabajadorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Trabajador', function(Trabajador) {
                                return Trabajador.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('trabajador', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('trabajador.delete', {
                parent: 'trabajador',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/trabajador/trabajador-delete-dialog.html',
                        controller: 'TrabajadorDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Trabajador', function(Trabajador) {
                                return Trabajador.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('trabajador', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

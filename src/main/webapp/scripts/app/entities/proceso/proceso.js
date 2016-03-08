'use strict';

angular.module('modificandouserApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('proceso', {
                parent: 'entity',
                url: '/procesos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modificandouserApp.proceso.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/proceso/procesos.html',
                        controller: 'ProcesoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('proceso');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('proceso.detail', {
                parent: 'entity',
                url: '/proceso/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modificandouserApp.proceso.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/proceso/proceso-detail.html',
                        controller: 'ProcesoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('proceso');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Proceso', function($stateParams, Proceso) {
                        return Proceso.get({id : $stateParams.id});
                    }]
                }
            })
            .state('proceso.new', {
                parent: 'proceso',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/proceso/proceso-dialog.html',
                        controller: 'ProcesoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nombre: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('proceso', null, { reload: true });
                    }, function() {
                        $state.go('proceso');
                    })
                }]
            })
            .state('proceso.edit', {
                parent: 'proceso',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/proceso/proceso-dialog.html',
                        controller: 'ProcesoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Proceso', function(Proceso) {
                                return Proceso.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('proceso', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('proceso.delete', {
                parent: 'proceso',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/proceso/proceso-delete-dialog.html',
                        controller: 'ProcesoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Proceso', function(Proceso) {
                                return Proceso.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('proceso', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

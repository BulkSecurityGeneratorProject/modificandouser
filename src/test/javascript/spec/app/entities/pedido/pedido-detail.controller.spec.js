'use strict';

describe('Controller Tests', function() {

    describe('Pedido Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPedido, MockProceso, MockTrabajador, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPedido = jasmine.createSpy('MockPedido');
            MockProceso = jasmine.createSpy('MockProceso');
            MockTrabajador = jasmine.createSpy('MockTrabajador');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Pedido': MockPedido,
                'Proceso': MockProceso,
                'Trabajador': MockTrabajador,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("PedidoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modificandouserApp:pedidoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

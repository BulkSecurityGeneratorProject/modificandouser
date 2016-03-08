'use strict';

describe('Controller Tests', function() {

    describe('Proceso Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProceso, MockPedido, MockTrabajador;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProceso = jasmine.createSpy('MockProceso');
            MockPedido = jasmine.createSpy('MockPedido');
            MockTrabajador = jasmine.createSpy('MockTrabajador');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Proceso': MockProceso,
                'Pedido': MockPedido,
                'Trabajador': MockTrabajador
            };
            createController = function() {
                $injector.get('$controller')("ProcesoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modificandouserApp:procesoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

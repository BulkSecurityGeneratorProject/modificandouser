'use strict';

describe('Controller Tests', function() {

    describe('Trabajador Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTrabajador, MockProceso, MockPedido;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTrabajador = jasmine.createSpy('MockTrabajador');
            MockProceso = jasmine.createSpy('MockProceso');
            MockPedido = jasmine.createSpy('MockPedido');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Trabajador': MockTrabajador,
                'Proceso': MockProceso,
                'Pedido': MockPedido
            };
            createController = function() {
                $injector.get('$controller')("TrabajadorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modificandouserApp:trabajadorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

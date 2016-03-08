 'use strict';

angular.module('modificandouserApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-modificandouserApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-modificandouserApp-params')});
                }
                return response;
            }
        };
    });

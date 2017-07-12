(function() {
    'use strict';
    angular
        .module('simpleApp')
        .factory('Indent', Indent);

    Indent.$inject = ['$resource'];

    function Indent ($resource) {
        var resourceUrl =  'api/indents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

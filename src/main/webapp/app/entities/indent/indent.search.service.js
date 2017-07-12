(function() {
    'use strict';

    angular
        .module('simpleApp')
        .factory('IndentSearch', IndentSearch);

    IndentSearch.$inject = ['$resource'];

    function IndentSearch($resource) {
        var resourceUrl =  'api/_search/indents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

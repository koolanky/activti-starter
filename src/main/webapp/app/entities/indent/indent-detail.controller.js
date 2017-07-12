(function() {
    'use strict';

    angular
        .module('simpleApp')
        .controller('IndentDetailController', IndentDetailController);

    IndentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Indent', 'User'];

    function IndentDetailController($scope, $rootScope, $stateParams, previousState, entity, Indent, User) {
        var vm = this;

        vm.indent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('simpleApp:indentUpdate', function(event, result) {
            vm.indent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

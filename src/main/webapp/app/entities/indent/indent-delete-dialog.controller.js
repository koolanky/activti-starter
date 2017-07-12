(function() {
    'use strict';

    angular
        .module('simpleApp')
        .controller('IndentDeleteController',IndentDeleteController);

    IndentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Indent'];

    function IndentDeleteController($uibModalInstance, entity, Indent) {
        var vm = this;

        vm.indent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Indent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

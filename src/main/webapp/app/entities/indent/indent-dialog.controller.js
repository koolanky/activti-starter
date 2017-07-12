(function() {
    'use strict';

    angular
        .module('simpleApp')
        .controller('IndentDialogController', IndentDialogController);

    IndentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Indent', 'User'];

    function IndentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Indent, User) {
        var vm = this;

        vm.indent = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.indent.id !== null) {
                Indent.update(vm.indent, onSaveSuccess, onSaveError);
            } else {
                Indent.save(vm.indent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('simpleApp:indentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

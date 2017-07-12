(function() {
    'use strict';

    angular
        .module('simpleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('indent', {
            parent: 'entity',
            url: '/indent?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Indents'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/indent/indents.html',
                    controller: 'IndentController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('indent-detail', {
            parent: 'indent',
            url: '/indent/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Indent'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/indent/indent-detail.html',
                    controller: 'IndentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Indent', function($stateParams, Indent) {
                    return Indent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'indent',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('indent-detail.edit', {
            parent: 'indent-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/indent/indent-dialog.html',
                    controller: 'IndentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Indent', function(Indent) {
                            return Indent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('indent.new', {
            parent: 'indent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/indent/indent-dialog.html',
                    controller: 'IndentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                indentId: null,
                                materialType: null,
                                materialCode: null,
                                budgeted: null,
                                indents: null,
                                currentStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('indent', null, { reload: 'indent' });
                }, function() {
                    $state.go('indent');
                });
            }]
        })
        .state('indent.edit', {
            parent: 'indent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/indent/indent-dialog.html',
                    controller: 'IndentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Indent', function(Indent) {
                            return Indent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('indent', null, { reload: 'indent' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('indent.delete', {
            parent: 'indent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/indent/indent-delete-dialog.html',
                    controller: 'IndentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Indent', function(Indent) {
                            return Indent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('indent', null, { reload: 'indent' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

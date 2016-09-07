member.controller('RewardCtrl', ['$scope','$state','$window','$filter','MemberControllerService','TableService','localStorageService', function($scope,$state,$window,$filter,MemberControllerService,TableService,localStorageService) {

    $scope.pageSize=20;
    $scope.targeFlag = { 'org': false, items: {} };
    $scope.errorSerFlag = false;
    $scope.errorEmailFlag = false;
    $scope.errorPhoneFlag = false;
    $scope.member = null;
    $scope.ifmanager = localStorageService.get('ifmanager');
    /**
     * 获取分页数据
     * @param pageSize
     * @param currentNum
     * @param name
     * @param order
     */
    function page(currentNum){
        var onSuccess=function (data, status) {
            $scope.data = data.result.returnObject.datas;
            TableService.initTable($scope);
            TableService.initPage($scope,currentNum,data.result.returnObject.totalSize);
        };
        MemberControllerService.findPageReward({
            pageSize:$scope.pageSize,
            currentNum:currentNum,
            memberserialnumber:$scope.memberserialnumber}).then(onSuccess,null);
    }
    $scope.pageChanged = function() {
        page($scope.bigCurrentPage);
    };

    //初始化
    $scope.init=function(){
        page(1);
        $scope.checkboxes = { 'checked': false, items: {} };
    };

    //改变页面
    $scope.pageChanged = function() {
        page($scope.bigCurrentPage);
    };

    //搜索
    $scope.search=function(){
        $scope.checkboxes = { 'checked': false, items: {} };
        page(1);
    };
    $scope.init();
}]);
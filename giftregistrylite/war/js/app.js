/**
 * 
 */

var app = angular.module('giftreg', []);
	
app.controller('GiftController', [ '$http', '$scope', function($http, $scope) {
	$scope.gifts=[];
	$http.get('/_ah/api/giftendpoint/v1/gift').success(function(data) {
		$scope.gifts=data.items;
	});
}]);




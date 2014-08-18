var app = angular.module('giftreg', []);



app.controller('GiftController', [ '$http', '$scope', function($http, $scope) {
	this.gifts=[];

	var me = this;
	$http.get('/_ah/api/giftendpoint/v1/gift').success(function(data) {
		me.gifts=data.items;
	});
}]);

app.controller('FamilyMemberController', [ '$http', '$scope', function($http, $scope) {
	this.familymember=null;
	
	var me = this;
	$http.get('/_ah/api/familymemberendpoint/v1/familymember').success(function(data) {
		console.log(data);
		me.familymember=data;
	});
	
	this.isFamilyMember = function() {
		return this.familymember != null;
	};	

}]);

app.controller('FamilyController', [ '$http', '$scope', function($http, $scope) {
	this.family={};
	this.joinFamily = function() {
		console.log(this.family);
		$http.post('/_ah/api/familymemberendpoint/v1/familymember', this.family).success(function(data) {
			console.log(data);
		}).error(function(data) {
			console.log("error");
			console.log(data);
		});
	};
}]);




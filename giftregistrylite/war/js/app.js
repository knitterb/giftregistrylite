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
	
	this.isFamilyMember = function() {
		return this.familymember != null;
	};
	
	var me = this;
	$http.get('/_ah/api/familymemberendpoint/v1/familymember').success(function(data) {
		me.familymember=data;
	});
	

}]);

app.controller('FamilyController', [ '$http', '$scope', function($http, $scope) {
	this.family={};
	this.joinFamily = function() {
		console.log(this.family);
		this.familymember={
				family: this.family
		};
		console.log(this.familymember);
	}
}]);




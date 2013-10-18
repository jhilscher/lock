sap.ui.controller( "ui5.Register" ,{

	onInit: function(){

	},

	isUserRegistered: function () {
		
		var url = "/lock/api/service/regpolling";
		
		var isRegistered = false;
		
		$.ajax({
   			type: "POST",
   			url: url,
   			async: false,
   			complete: function (xhr, statusCode) {
   				if(xhr.status == '200') {
   					isRegistered = true;
   				} else {
   					isRegistered = false;
   				}
   			}
   		});
		
		return isRegistered;
	},
	
	showRegisterQR: function() {
		
		var url = "/lock/api/service/getregisterqr";
		
		var qrUrl = "";
		
		$.ajax({
   			type: "GET",
   			url: url,
   			async: false,
   			complete: function (xhr, statusCode) {
   				if(xhr.status == '200') {
   					qrUrl = xhr.responseText;
   				} else {
   					qrUrl = false;
   				}
   			}
   		});
		
		return qrUrl;
	}
	
	// onBeforeRendering: function(){
	//
	//},
	
	// onAfterRendering: function(){
	//
	//},
	
	// onExit: function(){
	//
	//}
});
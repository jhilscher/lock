sap.ui.controller( "ui5.Auth" ,{

	onInit: function(){

	},

	isUserAuthed: function () {
		
		var url = "/lock/api/service/authpolling";
		
		var isAuthed = false;
		
		$.ajax({
   			type: "POST",
   			url: url,
   			async: false,
   			complete: function (xhr, statusCode) {
   				if(xhr.status == '200') {
   					isAuthed = true;
   				} else {
   					isAuthed = false;
   				}
   			}
   		});
		
		return isAuthed;
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
	
	showAuthQR: function() {
		
		var url = "/lock/api/service/getauthqr";
		
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
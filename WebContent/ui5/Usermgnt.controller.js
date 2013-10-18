sap.ui.controller( "ui5.Usermgnt" ,{

//	onInit: function(){
//	
//	},

	// onBeforeRendering: function(){
	//
	//},
	
	// onAfterRendering: function(){
	//
	//},
	
	// onExit: function(){
	//
	//}
	
	loadData: function () {
		var json;
		var url = "/lock/api/service/getallusers";
		
		$.ajax({
   			type: "GET",
   			url: url,
   			async: false,
   			success: function (data) {
				json = data;
   			}
   		});
		
		return json;
		
	},
	
	removeClientId: function (userId, model) {
		
		var _this = this;
		
		$.ajax({
    			type: "POST",
    			url: "/lock/api/service/removeclientidfromuser",
    			data: 'id='+userId,
    			complete: function (xhr, statusCode) {
    				if(xhr.status == '200' || xhr.status == '201') {
    					model.setData({modelData: _this.loadData() });
    					
    				} 
    				else {
    					 sap.ui.commons.MessageBox.alert("Failed to delete mobile Client from user!");
    				}
    				
    			}
		});
		
	}
	
});
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
	

	
	removeClientId: function (userId, model, table) {
		
		var url = "/lock/api/service/getallusers";
		table.setBusy(true);
		
		$.ajax({
    			type: "POST",
    			url: "/lock/api/service/removeclientidfromuser",
    			data: 'id='+userId,
    			complete: function (xhr, statusCode) {
    				if(xhr.status == '200' || xhr.status == '201') {
    					//model.refresh(true);
    					model.loadData(url);
    					table.setBusy(false);
    					table.rerender();
    				} 
    				else {
    					 sap.ui.commons.MessageBox.alert("Failed to delete mobile Client from user!");
    				}
    				
    			}
		});
		
	}
	
});
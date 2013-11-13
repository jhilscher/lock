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
		
		table.setBusy(true);
		
		$.ajax({
    			type: "POST",
    			url: url_removeUser,
    			data: 'id='+userId + '&' + csrfToken,
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
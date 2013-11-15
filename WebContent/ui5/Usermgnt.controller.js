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
    			data: 'id='+userId + csrfToken,
    			complete: function (xhr, statusCode) {
    				if(xhr.status == '200' || xhr.status == '201') {
    					//model.refresh(true);
    					model.loadData(url_allUsers);
    					table.setBusy(false);
    					table.rerender();
    				} 
    				else {
    					 sap.ui.commons.MessageBox.alert("Failed to delete mobile Client from user!");
    				}
    				
    			}
		});
		
	}, 
	showLogs: function (userName) {
		
		this.displayLogTable(userName);
		
//		$.ajax({
//    			type: "GET",
//    			url: url_getloginlogs + userName + getToken,
//    			complete: function (xhr, statusCode) {
//    				if(xhr.status == '200' || xhr.status == '201') {
//    					
//    					this.displayLogTable(userName, xhr.data);
//
//    				} 
//    				else {
//    					 sap.ui.commons.MessageBox.alert("Failed to load logs from user!");
//    				}
//    				
//    			}
//		});
		
	},
	displayLogTable: function (userName) {
		var oLogDialog = new sap.ui.commons.Dialog({
			modal: true,
			width: "60%"
				});

		oLogDialog.setTitle("User Logs of " + userName);
        
		 var oTable2 = new sap.ui.table.Table({
	         	visibleRowCount: 7,
	         	firstVisibleRow: 3,
	         	selectionMode: sap.ui.table.SelectionMode.Single,
	         	navigationMode: sap.ui.table.NavigationMode.Paginator,
	         });

	         //Define the columns and the control templates to be used
	         oTable2.addColumn(new sap.ui.table.Column({
	         	label: new sap.ui.commons.Label({text: "Successful"}),
	         	template: new sap.ui.commons.CheckBox({
        			editable: false
    			}).bindProperty("checked", "success", function (sValue) { return !!sValue; }).bindProperty("valueState", "success", 
    					function (sValue) { return (!!sValue)?  sap.ui.core.ValueState.Success : sap.ui.core.ValueState.Error; }),
	         	sortProperty: "success",
	         	filterProperty: "success",
	         	width: "100px",
	         	
	         }));

	       //Define the columns and the control templates to be used
	         oTable2.addColumn(new sap.ui.table.Column({
	         	label: new sap.ui.commons.Label({text: "IP-Adress"}),
	         	template: new sap.ui.commons.TextView().bindProperty("text", "ipAdress"),
	         	sortProperty: "ipAdress",
	         	filterProperty: "ipAdress",
	         	width: "100px"
	         }));
		
	         //Define the columns and the control templates to be used
	         oTable2.addColumn(new sap.ui.table.Column({
	         	label: new sap.ui.commons.Label({text: "userAgent"}),
	         	template: new sap.ui.commons.TextView().bindProperty("text", "userAgent"),
	         	sortProperty: "userAgent",
	         	filterProperty: "userAgent",
	         	width: "100px"
	         }));

	       //Define the columns and the control templates to be used
	         oTable2.addColumn(new sap.ui.table.Column({
	         	label: new sap.ui.commons.Label({text: "timeStamp"}),
	         	template: new sap.ui.commons.TextView().bindProperty("text", "timeStamp"),
	         	sortProperty: "timeStamp",
	         	filterProperty: "timeStamp",
	         	width: "100px"
	         }));

	         
	         //Create a model and bind the table rows to this model
	         var oModel2 = new sap.ui.model.json.JSONModel();	
	 		
	         
	         // Load Data
	         oModel2.loadData(url_getloginlogs + userName + getToken);
	         oModel2.attachRequestCompleted(function () {
	         	oTable2.setBusy(false);
	         });
	         
	         
	         oTable2.setBusy(true);
	         oTable2.setModel(oModel2);
	         oTable2.bindRows("/");

	         
	         //Initially sort the table -> timestamp
	         oTable2.sort(oTable2.getColumns()[3]);

	         
	         oTable2.setBusy(false);
	         
	         oLogDialog.addContent(oTable2);
        
		oLogDialog.addButton(new sap.ui.commons.Button({
        	text: "Close", 
        	press: function(){
        		oLogDialog.close();
        		oLogDialog.destroyContent();
        	}}));
        
		oLogDialog.open();
	}
	
});
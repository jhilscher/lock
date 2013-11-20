sap.ui.jsview("ui5.Usermgnt", {
      
	getControllerName : function() {
         return "ui5.Usermgnt";
	},
      
	createContent : function(oController) {
	
		var elements = [];
		
		var title = new sap.ui.commons.TextView({
	            text : "User Management",
	            design : sap.ui.commons.TextViewDesign.H1
	    });
		 
		elements.push(title);
		 
		// add a divider
		elements.push(new sap.ui.commons.HorizontalDivider());
		
		
		// TABLE
		

		

		  //Create an instance of the table control
         var oTable2 = new sap.ui.table.Table({
         	title: "User Management",
         	visibleRowCount: 7,
         	firstVisibleRow: 3,
         	selectionMode: sap.ui.table.SelectionMode.Single,
         	navigationMode: sap.ui.table.NavigationMode.Paginator
         });

         //Define the columns and the control templates to be used
         oTable2.addColumn(new sap.ui.table.Column({
         	label: new sap.ui.commons.Label({text: "UserName"}),
         	template: new sap.ui.commons.TextView().bindProperty("text", "userName"),
         	sortProperty: "userName",
         	filterProperty: "userName",
         	width: "100px"
         }));

         oTable2.addColumn(new sap.ui.table.Column({
         	label: new sap.ui.commons.Label({text: "eMail"}),
         	template: new sap.ui.commons.TextView().bindProperty("text", "email"),
         	sortProperty: "email",
         	filterProperty: "email",
         	width: "100px"
         }));
         
         oTable2.addColumn(new sap.ui.table.Column({
         	label: new sap.ui.commons.Label({text: "Account Created At"}),
         	template: new sap.ui.commons.TextView().bindProperty("text", "createdAt"),
         	sortProperty: "createdAt",
         	filterProperty: "createdAt",
         	width: "100px",
         	hAlign: "Center"
         }));
         
         oTable2.addColumn(new sap.ui.table.Column({
        		label: new sap.ui.commons.Label({text: "Mobile Registered?"}),
        		template: new sap.ui.commons.CheckBox({
        			editable: false
        			}).bindProperty("checked", "isRegistered"),
        		sortProperty: "checked",
        		filterProperty: "checked",
        		width: "50px",
        		hAlign: "Center"
        	}));
         
         oTable2.addColumn(new sap.ui.table.Column({
         	label: new sap.ui.commons.Label({text: "Remove Mobile"}),
         	template: new sap.ui.commons.Button({
                text : "remove",
                tooltip : "Remove the mobile client from this user.",
                press : function(oEvent) {
                		var oContext = oEvent.getSource().getBindingContext();  
                		oController.removeClientId(oContext.getProperty('id'), oModel2, oTable2);
                	}
                
        	}).bindProperty("enabled", "isRegistered", function (sValue) { return !!sValue; }),
         	width: "60px"
         }));
         
         oTable2.addColumn(new sap.ui.table.Column({
          	label: new sap.ui.commons.Label({text: "Login Logs"}),
          	template:  new sap.ui.commons.Button({
                text : "show logs",
                tooltip : "Show the login logs of this user.",
                press : function(oEvent) {
                		var oContext = oEvent.getSource().getBindingContext();  
                		oController.showLogs(oContext.getProperty('userName'));
                	}
        	}).bindProperty("enabled", "isRegistered", function (sValue) { return !!sValue; }),
          	width: "50px",
    		hAlign: "Left"
          }));
         
         //Create a model and bind the table rows to this model
        var oModel2 = new sap.ui.model.json.JSONModel();	
		
        oTable2.setBusy(true);
        
        // Load Data
        oModel2.loadData(url_allUsers);
        oModel2.attachRequestCompleted(function () {
        	oTable2.setBusy(false);
        	oTable2.rerender();
        });
        
        //oModel2.setData({modelData: oController.loadData()});
        
        oTable2.setModel(oModel2);
        oTable2.bindRows("/");

        
        //Initially sort the table
        oTable2.sort(oTable2.getColumns()[0]);

        
        
        elements.push(oTable2);
		
        
		
		
		return elements;
		
	}
      
});
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
		
		var url = "/lock/api/service/getallusers";
		

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
        			}).bindProperty("checked", "identifier", function (sValue) { return !!sValue; }),
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
                
        	}).bindProperty("enabled", "identifier", function (sValue) { return !!sValue; }),
         	width: "60px"
         }));
         
         oTable2.addColumn(new sap.ui.table.Column({
          	label: new sap.ui.commons.Label({text: "Last log in"}),
          	template:  new sap.ui.commons.TextView().bindProperty("text", "identifier/loginAttempt"),
          	width: "100px",
    		hAlign: "Center"
          }));
         
         //Create a model and bind the table rows to this model
        var oModel2 = new sap.ui.model.json.JSONModel();	
		
        
        // Load Data
        oModel2.loadData(url);
        
        //oModel2.setData({modelData: oController.loadData()});
        
        oTable2.setModel(oModel2);
        oTable2.bindRows("/");

        
        //Initially sort the table
        oTable2.sort(oTable2.getColumns()[0]);

        oTable2.setBusy(false);
        
        elements.push(oTable2);
		
        
		
		
		return elements;
		
	}
      
});
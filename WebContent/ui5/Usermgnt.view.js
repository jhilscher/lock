sap.ui.jsview("ui5.Usermgnt", {
      
	getControllerName : function() {
         return "ui5.Usermgnt";
	},
      
	createContent : function(oController) {
	
		// container for all ui5-elements
		var elements = [];
		
		
		// title
		var title = new sap.ui.commons.TextView({
	            text : "User Management",
	            design : sap.ui.commons.TextViewDesign.H1
	    });
		 
		elements.push(title);
		 
		// add a divider
		elements.push(new sap.ui.commons.HorizontalDivider());
		
		 // CREATE TABLE
		
		 /**
		  * TABLE
		  */
         var oTable2 = new sap.ui.table.Table({
         	title: "User Management",
         	visibleRowCount: 7,
         	firstVisibleRow: 3,
         	selectionMode: sap.ui.table.SelectionMode.Single,
         	navigationMode: sap.ui.table.NavigationMode.Paginator
         });

         /**
          * USERNAME
          */
         oTable2.addColumn(new sap.ui.table.Column({
         	label: new sap.ui.commons.Label({text: "UserName"}),
         	template: new sap.ui.commons.TextView().bindProperty("text", "userName"),
         	sortProperty: "userName",
         	filterProperty: "userName",
         	width: "100px"
         }));

         /**
          * EMAIL
          */
         oTable2.addColumn(new sap.ui.table.Column({
         	label: new sap.ui.commons.Label({text: "eMail"}),
         	template: new sap.ui.commons.TextView().bindProperty("text", "email"),
         	sortProperty: "email",
         	filterProperty: "email",
         	width: "100px"
         }));
         
         /**
          * DATE CREATED
          */
         oTable2.addColumn(new sap.ui.table.Column({
         	label: new sap.ui.commons.Label({text: "Account Created At"}),
         	template: new sap.ui.commons.TextView().bindProperty("text", "createdAt"),
         	sortProperty: "createdAt",
         	filterProperty: "createdAt",
         	width: "100px",
         	hAlign: "Center"
         }));
         
        
         /**
          * COMBOX BOX FOR SETTINGS
          */
         var createComboBox = function () {
	         
	         // Create a ComboBox
	         var oComboBox1 = new sap.ui.commons.ComboBox();
	         
	         oComboBox1.setTooltip("securityLevel");
	         oComboBox1.setEditable(true);
	        
	         oComboBox1.setWidth("100px");
	         
	         var oItem = new sap.ui.core.ListItem({
	        	 key: -1
	         });
	         oItem.setText("not set");
	         oComboBox1.addItem(oItem); 
	         
	         oItem = new sap.ui.core.ListItem({
	        	 key: 0
	         });
	         oItem.setText("Only restricted");
	         
	         oComboBox1.addItem(oItem);
	         
	         oItem = new sap.ui.core.ListItem({
	        	 key: 1
	         });
	         oItem.setText("AlwaysOn");
	         
	         oComboBox1.addItem(oItem);
	         oItem = new sap.ui.core.ListItem({
	        	 key: 2
	         });
	         
	         oItem.setText("On risk");
	         oComboBox1.addItem(oItem);
	         
	         
	         oComboBox1.setSelectedKey(-1); // default
	         
	         return oComboBox1;
		 };

         
         /**
          * SETTINGS
          */
         oTable2.addColumn(new sap.ui.table.Column({
          	label: new sap.ui.commons.Label({text: "Security Settings"}),
          	template: createComboBox().bindProperty("selectedKey", "securityLevel")
          						.attachChange(function(oEvent){
          							var oContext = oEvent.getSource().getBindingContext();  
          							var val = this.getSelectedKey(); // geht
          							//alert(oContext.getProperty('userName') + "  --   " + val);
          							oController.setSettingsOfUser(oContext.getProperty('userName'), val);
          						}),
          	sortProperty: "securityLevel",
          	filterProperty: "securityLevel",
          	width: "50px",
          	hAlign: "Center"
          }));
         
         /**
          * REGISTERED
          */
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
         
         /**
          * REMOVE
          */
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
         
         /**
          * LOGS
          */
         oTable2.addColumn(new sap.ui.table.Column({
          	label: new sap.ui.commons.Label({text: "Logon Logs"}),
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
         
         
		var createStatusBox = function () {
			         
			         // Create a ComboBox
			         var statusBox = new sap.ui.commons.ComboBox();
			         
			         statusBox.setTooltip("securityLevel");
			         statusBox.setEditable(true);
			        
			         statusBox.setWidth("100px");
			         
			         var oItem = new sap.ui.core.ListItem({
			        	 key: 0
			         });
			         oItem.setText("Not Set");
			         statusBox.addItem(oItem); 
			         
			         oItem = new sap.ui.core.ListItem({
			        	 key: 1
			         });
			         oItem.setText("OK");
			         
			         statusBox.addItem(oItem);
			         
			         oItem = new sap.ui.core.ListItem({
			        	 key: 2
			         });
			         oItem.setText("Blocked");
			         
			         statusBox.addItem(oItem);
			         
			         statusBox.setSelectedKey(1); // default
			         
			         return statusBox;
		 };
         
         /**
          * STATUS
          */
         oTable2.addColumn(new sap.ui.table.Column({
           	label: new sap.ui.commons.Label({text: "Account Status"}),
           	template:  createStatusBox().bindProperty("selectedKey", "securityLevel")
				.attachChange(function(oEvent){
						var oContext = oEvent.getSource().getBindingContext();  
						var val = this.getSelectedKey(); // geht
						//alert(oContext.getProperty('userName') + "  --   " + val);
						oController.setStatusOfUser(oContext.getProperty('userName'), val);
					}),
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
        	oTable2.setModel(oModel2);
        	oTable2.bindRows("/");
        });
        
        //Initially sort the table
        oTable2.sort(oTable2.getColumns()[0], sap.ui.table.SortOrder.Descending);

        elements.push(oTable2);
		
		return elements;
		
	}
      
});
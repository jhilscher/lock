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
         	title: "Administrate User Access",
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
         	width: "50px"
         }));

         /**
          * EMAIL
          */
         oTable2.addColumn(new sap.ui.table.Column({
         	label: new sap.ui.commons.Label({text: "eMail"}),
         	template: new sap.ui.commons.TextView().bindProperty("text", "email"),
         	sortProperty: "email",
         	filterProperty: "email",
         	width: "80px"
         }));
         
         /**
          * DATE CREATED
          */
         oTable2.addColumn(new sap.ui.table.Column({
         	label: new sap.ui.commons.Label({text: "Account Created At"}),
         	template: new sap.ui.commons.TextView().bindProperty("text", "createdAt"),
         	sortProperty: "createdAt",
         	filterProperty: "createdAt",
         	width: "80px",
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
	        	 key: "default"
	         });
	         oItem.setText("not set");
	         oComboBox1.addItem(oItem); 
	         
	         oItem = new sap.ui.core.ListItem({
	        	 key: "key"+0
	         });
	         oItem.setText("Only restricted");
	         
	         oComboBox1.addItem(oItem);
	         
	         oItem = new sap.ui.core.ListItem({
	        	 key: "key"+1
	         });
	         oItem.setText("Always");
	         
	         oComboBox1.addItem(oItem);
	         oItem = new sap.ui.core.ListItem({
	        	 key: "key"+2
	         });
	         
	         oItem.setText("Risk-based");
	         oComboBox1.addItem(oItem);
	         
	         
	         oComboBox1.setSelectedKey("default"); // default
	         
	         return oComboBox1;
		 };
         
         /**
          * SETTINGS
          */
         oTable2.addColumn(new sap.ui.table.Column({
          	label: new sap.ui.commons.Label({text: "Security Settings"}),
          	template: createComboBox().bindProperty("selectedKey", "securityLevel", function (sValue) { return 'key' + sValue; })
          						.attachChange(function(oEvent){
          							var oContext = oEvent.getSource().getBindingContext();  
          							var val = this.getSelectedKey(); // geht
          							//alert(oContext.getProperty('userName') + "  --   " + val);
          							oController.setSettingsOfUser(oContext.getProperty('userName'), val.charAt( val.length - 1 ));
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
         
         // toggle button
         var ToggleButton = function () {
        	 var btn = new sap.ui.commons.ToggleButton({
                 text : "Allow",
                 tooltip : "Allow User to register mobile",
                 press : function(oEvent) {
                 		var oContext = oEvent.getSource().getBindingContext();  
                 		oController.allowUserToRegister(oContext.getProperty('userName'), this.getPressed());
                 	}
         	});

        	 return btn;
         };
         
         /**
          * ALLOW TO REGISTER
          */
         oTable2.addColumn(new sap.ui.table.Column({
          	label: new sap.ui.commons.Label({text: "Allow User to register mobile"}),
          	template: ToggleButton().bindProperty("pressed", "allowRegister", function (sValue) { return !!sValue; }),
          	width: "50px",
    		hAlign: "Left"
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
			         
			         statusBox.setTooltip("Account Status");
			         statusBox.setEditable(true);
			        
			         statusBox.setWidth("100px");
			         
			         var oItem = new sap.ui.core.ListItem({
			        	 key: 'key' + 0
			         });
			         oItem.setText("Not Set");
			         statusBox.addItem(oItem); 
			         
			         oItem = new sap.ui.core.ListItem({
			        	 key: 'key' + 1
			         });
			         oItem.setText("OK");
			         
			         statusBox.addItem(oItem);
			         
			         oItem = new sap.ui.core.ListItem({
			        	 key: 'key' + 2
			         });
			         oItem.setText("Blocked");
			         
			         statusBox.addItem(oItem);
			         
			         statusBox.setSelectedKey("keyO"); // default
			         
			         return statusBox;
		 };
         
         /**
          * STATUS
          */
         oTable2.addColumn(new sap.ui.table.Column({
           	label: new sap.ui.commons.Label({text: "Account Status"}),
           	template:  createStatusBox().bindProperty("selectedKey", "status", function (sValue) { return 'key' + sValue; })
				.attachChange(function(oEvent){
						var oContext = oEvent.getSource().getBindingContext();  
						var val = this.getSelectedKey(); // geht
						//alert(oContext.getProperty('userName') + "  --   " + val);
						oController.setStatusOfUser(oContext.getProperty('userName'), val.charAt( val.length - 1 ));
					}),
           	width: "50px",
     		hAlign: "Left"
           }));
         
         //Create a model and bind the table rows to this model
        var oModel2 = new sap.ui.model.json.JSONModel();	
		oTable2.bindRows("/");
        oTable2.setModel(oModel2);
        oTable2.setBusy(true);
        
        // Load Data
        oModel2.loadData(url_allUsers);
        oModel2.attachRequestCompleted(function () {
        	oTable2.setBusy(false);
        	oTable2.rerender();
        	
        	//Initially sort the table
        	oTable2.sort(oTable2.getColumns()[0], sap.ui.table.SortOrder.Descending);
        });
        
        

        elements.push(oTable2);
		
		return elements;
		
	}
      
});
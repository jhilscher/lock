/*
 * @author Joerg Hilscher
 */
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
	

	dialog: null,
	
	/*
	 * Deletes all client (mobile) related data of a user.
	 * Reloads and refreshes the table afterwards.
	 * 
	 * @param: userName 	userName of the user
	 * @param: model 		model of the table
	 * @param: table		table
	 * 
	 */
	removeClientId: function (userName, model, table) {
		
		table.setBusy(true);
		
		$.ajax({
    			type: "POST",
    			url: url_removeUser,
    			data: 'id='+userName + csrfToken,
    			complete: function (xhr, statusCode) {
    				if(xhr.status == '200' || xhr.status == '201') {
    					//model.refresh(true);
    					model.loadData(url_allUsers);
    					table.setBusy(false);
    					table.rerender();
    					sap.ui.commons.MessageBox.alert("Successfully deleted the mobile Client.");
    				} 
    				else {
    					 sap.ui.commons.MessageBox.alert("Failed to delete mobile Client from user!");
    				}
    				
    			}
		});
		
	}, 
	
	createMenuBar: function (userName, selected) {
		
		var controller = this;
		
		var oSegmentedButton = new sap.ui.commons.SegmentedButton({
			buttons:[new sap.ui.commons.Button({id:"ButA",text:"User-Logs"}),
			         new sap.ui.commons.Button({id:"ButB", text:"Chart"}),
			         new sap.ui.commons.Button({id:"ButC",  text:"Timeline"})]});

		
		oSegmentedButton.setSelectedButton(selected);
		
		oSegmentedButton.attachSelect(function(oEvent) {
			switch (oEvent.getParameters().selectedButtonId) {
				case "ButA": controller.showLogs(userName); break;
				case "ButB": controller.showCharts(userName); break;
				case "ButC": controller.showTimeLine(userName); break;
				default: fn1(userName);
			}
		});
		
		return oSegmentedButton;
	},
	
	createDialog: function (title, userName, selected) {
		
		var d = this.dialog;
		
		if (this.dialog == null) {
			
			// create dialog
			d = new sap.ui.commons.Dialog({
				modal: true,
				width: "60%"
					});
			
	         d.addButton(new sap.ui.commons.Button({
		        	text: "Close", 
		        	press: function(){
		        		d.close();
		        		d.destroyContent();
		        	}}));
		}
		
		d.destroyContent();
		d.removeAllContent();
		d.addContent(this.createMenuBar(userName, selected));
		d.setTitle(title);
		
		d.open();
		
		this.dialog = d;
		
	},
	
	/*
	 * Load user logs as json and shows the data as a chart
	 *
	 * @param: userName		userName of the user, who's data to load
	 */
	showCharts: function (userName) { 
		

		this.createDialog("User Chart of " + userName, userName, "ButB");
		
		
		
		//Create a model and bind the table rows to this model
        var oModel2 = new sap.ui.model.json.JSONModel();	
		
        // Load Data
        oModel2.loadData(url_getlogincharts + userName + getToken);
        
        // callback function
        oModel2.attachRequestCompleted(function () {

        });
        
        var oDataset = new sap.viz.ui5.data.FlattenedDataset({
        		dimensions : [{
                                axis : 1, 
                                name : 'Ip', 
                                value : "{ipAdress}"
        		}],
    			measures : [{
                                name : 'success', 
                                value : '{success}'},
                        {
                                name : 'fails', 
                                value : '{fail}'
                        } 
                ],
                data : { path : "/" }      
        });

	    // create a Bar chart
	    // you also might use Combination, Line, StackedColumn100, StackedColumn or Column
	    // for Donut and Pie please remove one of the two measures in the above Dataset.  
        var oBarChart = new sap.viz.ui5.Bar({
                width : "80%",
                height : "400px",
                plotArea : {
                //'colorPalette' : d3.scale.category20().range()
                },
                title : {
                        visible : true,
                        text : 'Logons by ' + userName 
                },
                dataset : oDataset
        });

        // attach the model to the chart and display it
        oBarChart.setModel(oModel2);
        
        // add chart to dialog
        this.dialog.addContent(oBarChart);

		
	},
	
	/*
	 * Load user logs as json and shows the data as a chart
	 *
	 * @param: userName		userName of the user, who's data to load
	 */
	showTimeLine: function (userName) { 
		
		this.createDialog("Logon Timeline of " + userName, userName, "ButC");
		
		
		
		//Create a model and bind the table rows to this model
        var oModel2 = new sap.ui.model.json.JSONModel();	
		
        // Load Data
        oModel2.loadData(url_getlogintimeline + userName + getToken);
        
        // callback function
        oModel2.attachRequestCompleted(function () {

        });
        
        var oDataset = new sap.viz.ui5.data.FlattenedDataset({
        		dimensions : [{
                                axis : 1, 
                                name : 'Date', 
                                value : "{date}"
        		}],
    			measures : [{
                                name : 'success', 
                                value : '{success}'},
                        {
                                name : 'fails', 
                                value : '{fail}'
                        } 
                ],
                data : { path : "/" }      
        });

	    // create a Bar chart
	    // you also might use Combination, Line, StackedColumn100, StackedColumn or Column
	    // for Donut and Pie please remove one of the two measures in the above Dataset.  
        var oBarChart = new sap.viz.ui5.Line({
                width : "80%",
                height : "400px",
                plotArea : {
                //'colorPalette' : d3.scale.category20().range()
                },
                title : {
                        visible : true,
                        text : 'Logons by ' + userName 
                },
                dataset : oDataset
        });

        // attach the model to the chart and display it
        oBarChart.setModel(oModel2);
        
        
        this.dialog.addContent(oBarChart);
	},
	
	/*
	 * Shows a table with the logs of a user.
	 * 
	 * @param: userName		userName of the user, who's data to load
	 */
	showLogs: function (userName) {
		

		this.createDialog("User Logs of " + userName, userName, "ButA");
        
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
	         	oTable2.rerender();
	         });
	         
	         
	         oTable2.setBusy(true);
	         oTable2.setModel(oModel2);
	         oTable2.bindRows("/");

	         
	         //Initially sort the table -> timestamp desc
	         oTable2.sort(oTable2.getColumns()[3], sap.ui.table.SortOrder.Descending);

	         
	         oTable2.setBusy(false);
	         
	         this.dialog.addContent(oTable2);
        
	        

	}
	
});
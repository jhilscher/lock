sap.ui.jsview("ui5.Start", {
      
	getControllerName : function() {
         return "ui5.Start";
	},
      
	createContent : function(oController) {
	
		 var title = new sap.ui.commons.TextView({
	            text : "LOCK Webapp",
	            design : sap.ui.commons.TextViewDesign.H1
	     });
		 
		 return title;
		
	}
      
});
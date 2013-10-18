sap.ui.jsview("ui5.Register", {
     
	info: '',
	
	regButton: '',
	
	getControllerName : function() {
         return "ui5.Register";
	},
      
	createContent : function(oController) {
	
		var elements = [];
		
		var _this = this;
		
		var title = new sap.ui.commons.TextView({
	            text : "Register",
	            design : sap.ui.commons.TextViewDesign.H1
	     });
		 
		 elements.push(title);
		 
		 // add a divider
		 elements.push(new sap.ui.commons.HorizontalDivider());
		 
		 _this.info = new sap.ui.commons.TextView({
	            design : sap.ui.commons.TextViewDesign.bold
	     });
		 
		 var isRegistered = oController.isUserRegistered();
		 
		 _this.info.setText(isRegistered? "User is already registered!" : "User is not registered.");
		 
		 if (!isRegistered) {
			 	_this.regButton = new sap.ui.commons.Button({
			        text : "Register",
			        tooltip : "This will show a QR-Code to register your smartphone",
			        press : function (event) { _this.openQRCode(oController); }
			});	
			 
			elements.push(_this.regButton); 
		 }
		 
		 elements.push(_this.info);
		 
		 return elements;		
	},
	
	openQRCode: function (oController) {
		
		var _this = this;
		
		var qrUrl = oController.showRegisterQR();
	
		var oQRDialog = new sap.ui.commons.Dialog({modal: true});

		oQRDialog.setTitle("Scan to register.");
        

		if (qrUrl) {
			var oImage = new sap.ui.commons.Image();
			oImage.setSrc(qrUrl);
			oImage.setTooltip("Scan this QR-Code to register your Smartphone.");
			oImage.setDecorative(false);
			oQRDialog.addContent(oImage);
			_this.regButton.setEnabled(false);
		} else {
			 var oText = new sap.ui.commons.TextView({text: "Error. Maybe you don't have the rights to register."});
			 oQRDialog.addContent(oText);
		}
        
		oQRDialog.addButton(new sap.ui.commons.Button({
        	text: "OK", 
        	press: function(){
        		oQRDialog.close();
        		oQRDialog.destroyContent();
        	}}));
        
		oQRDialog.open();
		
		var interval;
		
		function request () {
			if (oController.isUserRegistered()) {
				oQRDialog.removeContent(oImage);
				
				var successText = new sap.ui.commons.TextView({text: "Successfully registered!"});
				oQRDialog.addContent(successText);
				_this.info.setText("Your smartphone is registered.");

				 // stop interval
				clearInterval(interval);
			}
		}
		
		if (qrUrl) 
			interval = setInterval(request, 3000);

		
	}
	
	
      
});
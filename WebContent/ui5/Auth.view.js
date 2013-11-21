sap.ui.jsview("ui5.Auth", {
     
	info: '',
	
	authButton: '',
	
	getControllerName : function() {
         return "ui5.Auth";
	},
      
	createContent : function(oController) {
	
		var elements = [];
		
		var _this = this;
		
		var title = new sap.ui.commons.TextView({
	            text : "Log on",
	            design : sap.ui.commons.TextViewDesign.H1
	     });
		 
		 elements.push(title);
		 
		 // add a divider
		 elements.push(new sap.ui.commons.HorizontalDivider());
		 
		 _this.info = new sap.ui.commons.TextView({
	            design : sap.ui.commons.TextViewDesign.bold
	     });
		 
		 var isRegistered = oController.isUserRegistered();
		 
		 _this.info.setText(isRegistered? "You are registered." : "You are not registered.");
		 
		 if (isRegistered) {
			 	_this.authButton = new sap.ui.commons.Button({
			        text : "Log On",
			        tooltip : "This will show a QR-Code to log in with your smartphone",
			        press : function (event) { _this.openQRCode(oController); }
			});	
			 
			elements.push(_this.authButton); 
		 }
		 
		 elements.push(_this.info);
		 
		 return elements;		
	},
	
	openQRCode: function (oController) {
		
		var _this = this;
		
		var qrUrl = oController.showAuthQR();
	
		var oQRDialog = new sap.ui.commons.Dialog({modal: true});

		oQRDialog.setTitle("Scan to log on.");
        

		if (qrUrl) {
			var oImage = new sap.ui.commons.Image("qrCodeAuth");
			oImage.setSrc(qrUrl);
			oImage.setTooltip("Scan this QR-Code to login.");
			oImage.setDecorative(false);
			oQRDialog.addContent(oImage);
			_this.authButton.setEnabled(false);
		} else {
			 var oText = new sap.ui.commons.TextView({text: "Error. Maybe you don't have the rights to log in."});
			 oQRDialog.addContent(oText);
		}
        
		oQRDialog.addButton(new sap.ui.commons.Button({
        	text: "OK", 
        	press: function(){
        		oQRDialog.close();
        		oQRDialog.destroyContent();
        		
        		// stop interval
				clearInterval(interval);
        	}}));
        
		oQRDialog.open();
		
		var interval;
		
		
		// check if user is authed
		function request () {
			if (oController.isUserAuthed()) {
				oQRDialog.removeContent(oImage);
				
				var successText = new sap.ui.commons.TextView({text: "Successfully logged in!"});
				oQRDialog.addContent(successText);
				_this.info.setText("Your are logged in!");

				 // stop interval
				clearInterval(interval);
				clearInterval(disposeinterval);

				
				// insert redirect button
				oQRDialog.addButton(new sap.ui.commons.Button({
		        	text: "REDIRECT", 
		        	press: function(){
		        		window.location.href = "/lock/restricted";
		        	}}));
			}
		}
		
		// disable after 2 mins
		var disposeinterval = setInterval(dispose, 2 * 1000 * 60);
		
		// 
		function dispose() {
			oQRDialog.close();
    		oQRDialog.destroyContent();
    		
    		// stop interval
			clearInterval(interval);
			clearInterval(disposeinterval);
			sap.ui.commons.MessageBox.alert("The time to register has run out.");
		}
		
		if (qrUrl) 
			interval = setInterval(request, 3000);

		
	}
	
	
      
});
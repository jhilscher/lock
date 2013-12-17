sap.ui.jsview("ui5.Profile", {
     
	info: '',
	
	regButton: '',
	
	getControllerName : function() {
         return "ui5.Profile";
	},
      
	createContent : function(oController) {
	
		var elements = [];
		
		var _this = this;
		
		var title = new sap.ui.commons.TextView({
	            text : "Profile",
	            design : sap.ui.commons.TextViewDesign.H1
	     });
		 
		 elements.push(title);
		 
		 // add a divider
		 elements.push(new sap.ui.commons.HorizontalDivider());
		 
		 // *** TAB LAYOUT ***
		 
		 // Create a TabStrip instance
		 var tabLayout = new sap.ui.commons.TabStrip();
		 
		 tabLayout.setWidth("60%");
		 tabLayout.setHeight("400px");
		 
		 tabLayout.attachClose( function (oEvent) {
		 	var oTabStrip = oEvent.oSource;
		 	oTabStrip.closeTab(oEvent.getParameter("index"));
		 });
		 
		 
		 // 1: SETTINGS TAB
		 
		 var layoutSetting = new sap.ui.commons.layout.MatrixLayout({columns: 2, width: "100%"});
		 layoutSetting.setWidths(['200px']);
		 
		 var sLabel = new sap.ui.commons.TextView({
			 text: 'Security Settings',
			 design : sap.ui.commons.TextViewDesign.Bold});
		 
		 var sButton = new sap.ui.commons.Button({
				text : "Open Settings",
				tooltip : "Open Settings",
				press : function() { openSettings(); }
			});
		 
		 layoutSetting.createRow(sLabel, sButton);
		 
		 // ROW USERNAME
		 layoutSetting.createRow(new sap.ui.commons.TextView({
			 text: 'UserName',
			 design : sap.ui.commons.TextViewDesign.Bold}),
			 new sap.ui.commons.Label({
				 text: userDataModel.getProperty("/userName")}));

		 // ROW EMAIL
		 layoutSetting.createRow(new sap.ui.commons.TextView({
			 text: 'eMail',
			 design : sap.ui.commons.TextViewDesign.Bold}),
			 new sap.ui.commons.Label({
				 text: userDataModel.getProperty("/email")}));
		 
		 // ROW IS REGISTERED
		 layoutSetting.createRow(new sap.ui.commons.TextView({
			 text: 'Registered Mobile:',
			 design : sap.ui.commons.TextViewDesign.Bold}),
			 new sap.ui.commons.CheckBox({
				 checked : !!userDataModel.getProperty("/isRegistered"),
				 editable: false}));
		 
		 // ROW ALLOWED TO REGISTER
		 layoutSetting.createRow(new sap.ui.commons.TextView({
			 text: 'Allowed to Register:',
			 design : sap.ui.commons.TextViewDesign.Bold}),
			 new sap.ui.commons.CheckBox({
				 checked : !!userDataModel.getProperty("/allowedRegister"),
				 editable: false}));
		 
		 tabLayout.createTab("General", layoutSetting);

		 // 2: REGISTER TAB
		 
		 var layoutRegister = new sap.ui.commons.layout.MatrixLayout({columns: 2, width: "100%"});
		 layoutRegister.setWidths(['200px']);
		 
		 
		 _this.info = new sap.ui.commons.TextView({
	            design : sap.ui.commons.TextViewDesign.Bold
	            
	     });
		 
		 var isRegistered = oController.isUserRegistered();
		 
		 _this.info.setText(isRegistered? "You are already registered!" : "You are not registered.");
		 
		 
		 _this.regButton = new sap.ui.commons.Button({
		        text : "Register",
		        tooltip : "This will show a QR-Code to register your smartphone",
		        press : function (event) { _this.openQRCode(oController); },
		        enabled: !isRegistered
		 });	
			 
			//elements.push(_this.regButton); 
		 
		 layoutRegister.createRow(_this.info, _this.regButton);
		 tabLayout.createTab("Mobile Registration", layoutRegister);
		 
		 //elements.push(_this.info);
		 
		 
		 elements.push(tabLayout);
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
			//_this.regButton.setEnabled(false);
		} else {
			 var oText = new sap.ui.commons.TextView({text: "Error. Maybe you don't have the rights to register."});
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
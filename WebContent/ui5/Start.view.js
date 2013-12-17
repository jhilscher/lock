sap.ui.jsview("ui5.Start", {
      
	getControllerName : function() {
         return "ui5.Start";
	},
      
	createContent : function(oController) {
	
		var elements = [];
		
		 var title = new sap.ui.commons.TextView({
	            text : "LOCK Sample App",
	            design : sap.ui.commons.TextViewDesign.H1
	     });
		 
		 
		 var oImage = new sap.ui.commons.Image();
		 oImage.setSrc("/lock/ui5/Icon256.png");
		 
		 elements.push(title);
		 elements.push(new sap.ui.commons.HorizontalDivider());
		 
		 // get model allowedToRegister
//		 var modelRegister = userDataModel.getProperty("/allowedRegister");
//		 
//		 if (modelRegister) {
//			 
//			 var infoBar = new sap.ui.commons.TextView({
//				 	semanticColor: sap.ui.commons.TextViewColor.Positive,
//					design: sap.ui.commons.TextViewDesign.H1,
//					text: "You can now register your smartphone."
//			 });
//			 
//			 elements.push(infoBar);
//		 }
		 
		 
		 
		 // HTML TEXT
		 var sHtmlText = '<h2>Lock Sample App</h2>';
			sHtmlText+= 'this app will demonstrate the "lock" two-factor authentification (2FA).';
			sHtmlText += '<br>Some facts:';
			sHtmlText += '<ul><li>this app is running on HANA Cloud Platform</li><li>all sensitive data is hosted locally</li>';
			sHtmlText += '<li>to use 2FA get and register the Android lock app</li></ul>';

			
			var oFTV1 = new sap.ui.commons.FormattedTextView();
			//set the text with placeholders inside
			oFTV1.setHtmlText(sHtmlText);

		 
		 // MATRIX
		 var oMatrix = new sap.ui.commons.layout.MatrixLayout({layoutFixed: true, width: '800px', columns: 2});
		 oMatrix.setWidths('270px', '330px');

		 oMatrix.createRow(oImage, oFTV1);
		 
		 elements.push(oMatrix);
		 
		 return elements;
	}
      
});
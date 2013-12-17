
// Settings Dialog
var openSettings = function () {
	
	var saveSettings = function(buttonId) {
		
		var level = buttonId.split("_").pop() - 1;
		
		$.ajax({
			type: "POST",
			url: url_saveSettings,
			data: 'level=' + level + csrfToken,
			complete: function (xhr, statusCode) {
				if(xhr.status == '200' || xhr.status == '201') {
					sap.ui.commons.MessageBox.alert("Settings saved.");	
				} 
				else {
					 sap.ui.commons.MessageBox.alert("Failed to save settings.");
				}
				
			}
	});
		
	};
	
	var settingsDialog = new sap.ui.commons.Dialog({
		modal: true,
		width: "340px"
			});

	settingsDialog.setTitle("Settings");
    
	var info = new sap.ui.commons.TextView({
        text : "Select authentification level:",
        design : sap.ui.commons.TextViewDesign.H3
	});
	
	// add to dialog
    settingsDialog.addContent(info);
    settingsDialog.addContent(new sap.ui.commons.HorizontalDivider());
    
    
	var oSegmentedButton = new sap.ui.commons.SegmentedButton({
		id:"SBText",
		buttons:[
		         new sap.ui.commons.Button({id:"But_1", text:"Only restricted"}),
		         new sap.ui.commons.Button({id:"But_2", text:"Always"}),
		         new sap.ui.commons.Button({id:"But_3", text:"On risk"})
	]});

	var activButton = "But_1";
	
	var modelLevel = userDataModel.getProperty("/securityLevel");
	
	switch (modelLevel) {
		case 0: activButton = "But_1";
				break;
		case 1: activButton = "But_2";
				break;
		case 2: activButton = "But_3";
				break;
		default: activButton = "But_1";
				break;
	}
	
    oSegmentedButton.setSelectedButton(activButton);
	
	// add to dialog
    settingsDialog.addContent(oSegmentedButton);
	
    // add buttons
	settingsDialog.addButton(new sap.ui.commons.Button({
    	text: "Cancel", 
    	press: function(){
    		settingsDialog.close();
    		settingsDialog.destroyContent();
    	}}));
    
	settingsDialog.addButton(new sap.ui.commons.Button({
    	text: "Save", 
    	press: function(){
    		saveSettings(oSegmentedButton.getSelectedButton());
    		settingsDialog.close();
    		settingsDialog.destroyContent();
    	}}));
	
	settingsDialog.setShowCloseButton(false);
	
	settingsDialog.open();
	
};
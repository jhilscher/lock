		
	/*
	 * Public Shell
	 */	
	var shell = new sap.ui.ux3.Shell("userShell", {
			    appTitle : "Lock Sample App",
			    
			    
			    worksetItems : [ new sap.ui.ux3.NavigationItem({
			        key : "start",
			        id : "start",
			        text : "Start"
			    }), new sap.ui.ux3.NavigationItem({
			        key : "lock",
			        text : "Lock",
			        subItems : [ new sap.ui.ux3.NavigationItem({
			            key : "lockregister",
			            id : "lockregister",
			            text : "Register"
			        }), new sap.ui.ux3.NavigationItem({
			            key : "locklogin",
			            id : "locklogin",
			            text : "Log In"
			        }) ]
			    }), new sap.ui.ux3.NavigationItem({
			        key : "usermanagement",
			        text : "User Management"})
			    ],
			    showFeederTool : false,
			    showInspectorTool : false,
			    showSearchTool : false,
			    showLogoutButton : true,
			    headerItems : [ new sap.ui.commons.TextView({
			        text : "{/userName}"
			    }),
			    new sap.ui.commons.TextView().bindProperty("text", "{/isLoggedIn}", function(sValue) {
			    	if (sValue == null || sValue == "false")
			    		return "not logged in";
			    	else
			    		return "logged in";
			    })],
			    
			    
				logout: function(){
					logout();
				},
		});    

		
		
		// user data model
		var userDataModel = new sap.ui.model.json.JSONModel();

		// to get the current user
		function loadUserData() {
			userDataModel.loadData("/lock/api/service/getcurrentuser");
			sap.ui.getCore().setModel(userDataModel);
		}

		// Select on Shell Menu Items
		shell.attachWorksetItemSelected(function(oEvent) {
		    var key = oEvent.getParameter("key");	
		    setShellContent(key);
		});
		
		
		// return View depending on key
		// selects the item from the shell
		var setShellContent = function (key) {
			var view;
			
			if (key == "lockregister")
				view = sap.ui.view({id:"view_lock_1", viewName:"ui5.Register", type:sap.ui.core.mvc.ViewType.JS});
			else if (key == "locklogin")
				view = sap.ui.view({id:"view_lock_2", viewName:"ui5.Auth", type:sap.ui.core.mvc.ViewType.JS});
			else if (key == "usermanagement")
				view = sap.ui.view({id:"view_usermanagement_1", viewName:"ui5.Usermgnt", type:sap.ui.core.mvc.ViewType.JS});
			else {
				view = sap.ui.view({id:"view_start_1", viewName:"ui5.Start", type:sap.ui.core.mvc.ViewType.JS});	
				key = "start"; // set default value
			}
			
			try {
		        shell.setSelectedWorksetItem(key);
		    } catch (e) {
		    }
			
			shell.setContent(view, true);
			window.location.hash = jQuery.sap.encodeURL(key);
		};
		

		var logout = function () {
			window.location.href = "/lock/logout.xhtml";
		};


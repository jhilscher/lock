
		/*
		 * Admin Shell
		 */	
		var shell = new sap.ui.ux3.Shell("userShell", {
		    appTitle : "Lock Sample App",
		    appIcon: "https://sapui5.hana.ondemand.com/sdk/test-resources/sap/ui/ux3/demokit/images/SAPLogo.gif",
		    
		    worksetItems : [ new sap.ui.ux3.NavigationItem({
		        key : "start",
		        id : "start",
		        text : "Start"
		    }), new sap.ui.ux3.NavigationItem({
		        key : "profile",
		        text : "Profile",
		        id: "profile"
//		        subItems : [ new sap.ui.ux3.NavigationItem({
//		            key : "lockregister",
//		            id : "lockregister",
//		            text : "Register"
//		        }), new sap.ui.ux3.NavigationItem({
//		            key : "locklogin",
//		            id : "locklogin",
//		            text : "Log On"
//		        }) ]
		    })
		    ],
		    showFeederTool : false,
		    showInspectorTool : false,
		    showSearchTool : false,
		    showLogoutButton : true,
		    headerItems : [ 
		                   new sap.ui.commons.TextView({
						        text : "{/userName}"
						    }),
					    	new sap.ui.commons.TextView({
					    		text: "{/isLoggedIn}"
			    			}),
			    			new sap.ui.commons.TextView({
					    		text: "{/securityLevel}"
			    			}),
			    			new sap.ui.commons.Button({
			    				text:"Settings",
			    				tooltip:"Settings",
			    				press:function(oEvent){
			    					openSettings(oEvent);
			    				}
			    			}), 
			    			new sap.ui.commons.Button({
			    				text:"Restricted",
			    				tooltip:"Restricted",
			    				press:function(oEvent){
			    					window.location = "/lock/restricted";
			    				}
			    			}),
		    ],
		    
		    
			logout: function(){
				logout();
				},
		});
		

		// add admin menu
		if (isAdmin) {
			
			shell.addWorksetItem(
					new sap.ui.ux3.NavigationItem({
				    	id: "usermanagement",
				        key : "usermanagement",
				        text : "User Management"}));
			
		}
		
		// user data model
		var userDataModel = new sap.ui.model.json.JSONModel();

		// to get the current user
		function loadUserData() {
			userDataModel.loadData(url_currentUser);
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
			
			if (key == "profile")
				view = sap.ui.view({id:"view_profile_1", viewName:"ui5.Profile", type:sap.ui.core.mvc.ViewType.JS});
//			else if (key == "locklogin")
//				view = sap.ui.view({id:"view_lock_2", viewName:"ui5.Auth", type:sap.ui.core.mvc.ViewType.JS});
			else if (key == "usermanagement")
				view = sap.ui.view({id:"view_usermanagement_1", viewName:"ui5.Usermgnt", type:sap.ui.core.mvc.ViewType.JS});
			else {
				view = sap.ui.view({id:"view_start_1", viewName:"ui5.Start", type:sap.ui.core.mvc.ViewType.JS});	
				key = "start"; // set default value
			}
			
			try {
		        shell.setSelectedWorksetItem(key);
		        //shell.getWorksetItems().setKey(key);
		    } catch (e) {
		    }
			
			shell.setContent(view, true);
			window.location.hash = jQuery.sap.encodeURL(key);
		};
		

		var logout = function () {
			window.location.href = "/lock/logout.xhtml";
		};


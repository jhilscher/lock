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
			    }) ],
			    
			    
				logout: function(){
					alert("Logout Button has been clicked.\nThe application can now do whatever is required.");
				},
		});    

		
		
		// user data model
		var userDataModel = new sap.ui.model.json.JSONModel();

		// to get the current user
		function loadUserData() {
		    // async load currently logged in user
			userDataModel.loadData("/lock/api/service/getcurrentuser");
			sap.ui.getCore().setModel(userDataModel);
		    //loadRestDataSync(userDataModel, "/lock/api/service/getcurrentuser");
		}

		// Select on Shell Menu Items
		shell.attachWorksetItemSelected(function(oEvent) {
		    var key = oEvent.getParameter("key");
		   
		    setShellContent(key);
		    
		});
		
		
		
		// return View depending on key
		var setShellContent = function (key) {
			var view;
			
			if (key == "lockregister")
				view = sap.ui.view({id:"view_lock_1", viewName:"ui5.Register", type:sap.ui.core.mvc.ViewType.JS});
			else if (key == "locklogin")
				view = sap.ui.view({id:"view_lock_2", viewName:"ui5.Auth", type:sap.ui.core.mvc.ViewType.JS});
			else if (key == "usermanagement")
				view = sap.ui.view({id:"view_usermanagement_1", viewName:"ui5.Usermgnt", type:sap.ui.core.mvc.ViewType.JS});
			else 
				view = sap.ui.view({id:"view_start_1", viewName:"ui5.Start", type:sap.ui.core.mvc.ViewType.JS});	
			
			shell.setContent(view, true);
			window.location.hash = jQuery.sap.encodeURL(key);
		};
		







		/*
		var oNavigationBar1 = new sap.ui.ux3.NavigationBar({
                 items:[
                         new sap.ui.ux3.NavigationItem({
                        	 key:"item1", 
                        	 text:"Start",
                        	 href:"/lock"}),
                         new sap.ui.ux3.NavigationItem({
                        	 key:"item2", 
                        	 text:"User Management",
                        	 href:"/lock/adminArea.xhtml"}),
                         new sap.ui.ux3.NavigationItem({
                        	 key:"item3", 
                        	 text:"Restricted Area",
                        	 href:"/lock/restricted"}),
                       	 new sap.ui.ux3.NavigationItem({
                           	 key:"item4", 
                           	 text:"Register",
                           	 href:"/lock/registrationArea.xhtml"}),
                         new sap.ui.ux3.NavigationItem({
                        	 key:"item5", 
                        	 text:"Login",
                        	 href:"/lock/authArea.xhtml"}),
                         new sap.ui.ux3.NavigationItem({
                        	 key:"item6", 
                        	 text:"Logout",
                        	 href: "/lock/logout.xhtml"})
                 ],
                 select: function(oEvent) {
                     window.location.href = oEvent.getParameter("item").getHref();          
             	} 
         });

	         
         oNavigationBar1.placeAt("navBar");
		*/
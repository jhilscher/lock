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
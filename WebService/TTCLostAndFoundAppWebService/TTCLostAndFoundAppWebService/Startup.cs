using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.Owin;
using Owin;
using TTCLostAndFoundAppWebService.Models;

[assembly: OwinStartup(typeof(TTCLostAndFoundAppWebService.Startup))]

namespace TTCLostAndFoundAppWebService
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
            CreateRoles();
        }
        
        private void CreateRoles()
        {
            ApplicationDbContext context = new ApplicationDbContext();

            var roleManager = new RoleManager<IdentityRole>(new RoleStore<IdentityRole>(context));
            var userManager = new UserManager<ApplicationUser>(new UserStore<ApplicationUser>(context));


            // In Startup iam creating first Admin Role and creating a default Admin User     
            if (!roleManager.RoleExists("ItemValidator"))
            {
                // first we create Admin rool    
                var role = new Microsoft.AspNet.Identity.EntityFramework.IdentityRole
                {
                    Name = "ItemValidator"
                };
                roleManager.Create(role);

                //Here we create a Admin super user who will maintain the website                   

                var user = new ApplicationUser
                {
                    UserName = "riddhi@gmail.com",
                    Email = "riddhi@gmail.com",
                    FirstName = "Riddhi",
                    LastName = "Amin"
                };

                string userPWD = "Riddhi@123";

                var chkUser = userManager.Create(user, userPWD);

                //Add default User to Role Admin    
                if (chkUser.Succeeded)
                {
                    var result1 = userManager.AddToRole(user.Id, "ItemValidator");
                }
            }

            // creating Creating Manager role     
            if (!roleManager.RoleExists("BusGanitor"))
            {
                var role = new Microsoft.AspNet.Identity.EntityFramework.IdentityRole();
                role.Name = "BusGanitor";
                roleManager.Create(role);

                var user = new ApplicationUser
                {
                    UserName = "solanky@gmail.com",
                    Email = "solanky@gmail.com",
                    FirstName = "Solanky",
                    LastName = "Dareja"
                };

                string userPWD = "Solanky@123";

                var chkUser = userManager.Create(user, userPWD);

                //Add default User to Role Admin    
                if (chkUser.Succeeded)
                {
                    var result1 = userManager.AddToRole(user.Id, "BusGanitor");
                }

            }

            // creating Creating Employee role     
            if (!roleManager.RoleExists("BoothReceptionist"))
            {
                var role = new Microsoft.AspNet.Identity.EntityFramework.IdentityRole();
                role.Name = "BoothReceptionist";
                roleManager.Create(role);
                var user = new ApplicationUser
                {
                    UserName = "chintan@gmail.com",
                    Email = "chintan@gmail.com",
                    FirstName = "Chintan",
                    LastName = "Patel"
                };

                string userPWD = "Chintan@123";

                var chkUser = userManager.Create(user, userPWD);

                //Add default User to Role Admin    
                if (chkUser.Succeeded)
                {
                    var result1 = userManager.AddToRole(user.Id, "BoothReceptionist");
                }

            }

            if (!roleManager.RoleExists("Customer"))
            {
                var role = new Microsoft.AspNet.Identity.EntityFramework.IdentityRole();
                role.Name = "Customer";
                roleManager.Create(role);

              
            }

            if (context.IdBeIdVerifications.FirstOrDefault(c => c.Name == "Passport") == null)
            {
                context.IdBeIdVerifications.Add(new IdVerification
                {
                  Name = "Passport"
                });
            }

            if (context.IdBeIdVerifications.FirstOrDefault(c => c.Name == "Driving License") == null)
            {
                context.IdBeIdVerifications.Add(new IdVerification
                {
                    Name = "Driving License"
                });
            }


            if (userManager.FindByName("test@gmail.com") == null)
            {
                var user = new ApplicationUser()
                {
                    UserName = "test@gmail.com",
                    Email = "test@gmail.com",
                    FirstName = "Test",
                    LastName = "User",
                    Mobile = "1237813212",
                    Address = "18 Foxtrot Road"
                };

                IdentityResult result = userManager.Create(user, "Test@123");

                userManager.AddToRole(user.Id, "Customer");

            }
        }
    }
}

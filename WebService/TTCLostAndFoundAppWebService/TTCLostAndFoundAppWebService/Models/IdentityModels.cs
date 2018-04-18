using System.Collections;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Data.Entity;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;

namespace TTCLostAndFoundAppWebService.Models
{
    // You can add profile data for the user by adding more properties to your ApplicationUser class, please visit https://go.microsoft.com/fwlink/?LinkID=317594 to learn more.
    public class ApplicationUser : IdentityUser
    {
        [StringLength(30)]
        [Required]
        public string FirstName { get; set; }

        [StringLength(30)]
        [Required]
        public string LastName { get; set; }

        [StringLength(15)]
        public string Mobile { get; set; }

        [StringLength(120)]
        public string Address { get; set; }
        
        public ICollection<ClaimedItem> ClaimedItems { get; set; }

        public ICollection<FoundItem> FoundItems { get; set; }

        public async Task<ClaimsIdentity> GenerateUserIdentityAsync(UserManager<ApplicationUser> manager, string authenticationType)
        {
            // Note the authenticationType must match the one defined in CookieAuthenticationOptions.AuthenticationType
            var userIdentity = await manager.CreateIdentityAsync(this, authenticationType);
            // Add custom user claims here
            return userIdentity;
        }
    }
    
    public class ApplicationDbContext : IdentityDbContext<ApplicationUser>
    {
        public ApplicationDbContext()
            : base("DefaultConnection", throwIfV1Schema: false)
        {
        }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            Configuration.LazyLoadingEnabled = false;
            base.OnModelCreating(modelBuilder);
        }

        public DbSet<ClaimedItem> ClaimedItems { get; set; }
        public DbSet<FoundItem> FoundItems { get; set; }
        public DbSet<IdVerification> IdBeIdVerifications { get; set; }

        public static ApplicationDbContext Create()
        {
            return new ApplicationDbContext();
        }
    }
}
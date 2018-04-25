using System;
using System.Collections;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using TTCLostAndFoundAppWebService.Models;

namespace TTCLostAndFoundAppWebService.Controllers
{
    [Authorize]
    public class ClaimedItemsController : ApiController
    {
        private readonly ApplicationDbContext _db = new ApplicationDbContext();
        private ApplicationUserManager _userManager;

        public ApplicationUserManager UserManager
        {
            get => _userManager ?? Request.GetOwinContext().GetUserManager<ApplicationUserManager>();
            private set => _userManager = value;
        }

        [ResponseType(typeof(ClaimedItemBindingModel))]
        [System.Web.Http.HttpGet]
        public IEnumerable GetFoundItems(string query)
        {
            return _db.ClaimedItems.OrderByDescending(c => c.DateLost).Where(c =>
                c.Category.ToLower().Contains(query.ToLower())).Select(item => new FoundItemBindingModel
                {
                    Id = item.Id,
                    UserId = item.UserId,
                    DateLost = item.DateLost,
                    Description = item.Description,
                    Location = item.Location,
                    Category = item.Category,
                    Color = item.Color,
                    TrackingId = string.IsNullOrEmpty(item.TrackingId) ? "null" : item.TrackingId
                }).ToList();
        }


        // GET: api/ClaimedItems
        public IEnumerable GetClaimedItems()
        {
            return _db.ClaimedItems.OrderByDescending(c => c.DateLost).Select(item => new ClaimedItemBindingModel
            {
                Id = item.Id,
                UserId = item.UserId,
                DateLost = item.DateLost,
                Description = item.Description,
                Location = item.Location,
                Category = item.Category,
                Color = item.Color,
                TrackingId = string.IsNullOrEmpty(item.TrackingId) ? "null" : item.TrackingId
            }).ToList();
        }

        // GET: api/ClaimedItems/5
        [ResponseType(typeof(ClaimedItemBindingModel))]
        public IHttpActionResult GetClaimedItem(int id)
        {
            ClaimedItem claimedItem = _db.ClaimedItems.Find(id);
            if (claimedItem == null)
            {
                return NotFound();
            }

            var claimedModel = new ClaimedItemBindingModel
            {
                Category = claimedItem.Category,
                Color = claimedItem.Color,
                DateLost = claimedItem.DateLost,
                Description = claimedItem.Description,
                Location = claimedItem.Location,
                TrackingId = string.IsNullOrEmpty(claimedItem.TrackingId) ? "null" : claimedItem.TrackingId,
                UserId = claimedItem.UserId
            };
            return Ok(claimedModel);
        }

        // PUT: api/ClaimedItems/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutClaimedItem(int id, ClaimedItemAddBindingModel claimedItemModel)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            //if (id != claimedItemModel.Id)
            //{
            //    return BadRequest();
            //}

            var claimedItem = _db.ClaimedItems.FirstOrDefault(c => c.Id == id);

            if (claimedItem == null)
                return NotFound();

            claimedItem.Category = claimedItemModel.Category;
            claimedItem.Color = claimedItemModel.Color;
            claimedItem.DateLost = claimedItemModel.DateLost;
            claimedItem.Description = claimedItemModel.Description;
            claimedItem.Location = claimedItemModel.Location;
            
            _db.Entry(claimedItem).State = EntityState.Modified;

            try
            {
                _db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ClaimedItemExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/ClaimedItems
        [ResponseType(typeof(ClaimedItemAddBindingModel))]
        public IHttpActionResult PostClaimedItem(ClaimedItemAddBindingModel claimedItemModel)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var user = UserManager.FindByName(User.Identity.Name);

            var claimedItem = new ClaimedItem
            {
                Category = claimedItemModel.Category,
                Color = claimedItemModel.Color,
                DateLost = claimedItemModel.DateLost,
                Description = claimedItemModel.Description,
                Location = claimedItemModel.Location,
                UserId =  user.Id  
            };

            _db.ClaimedItems.Add(claimedItem);
            _db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = claimedItem.Id }, claimedItemModel);
        }

        // DELETE: api/ClaimedItems/5
        [ResponseType(typeof(ClaimedItem))]
        public IHttpActionResult DeleteClaimedItem(int id)
        {
            ClaimedItem claimedItem = _db.ClaimedItems.Find(id);
            if (claimedItem == null)
            {
                return NotFound();
            }

            _db.ClaimedItems.Remove(claimedItem);
            _db.SaveChanges();

            return Ok(claimedItem);
        }

        // its done
        [ResponseType(typeof(UserInfoViewModel))]
        public IHttpActionResult GetClaimUser(string userId)
        {
           var user = UserManager.FindById(userId);
            if (user == null)
                return BadRequest();
            return Ok(new UserInfoViewModel
            {
                Address = user.Address,
                Email = user.Email,
                FirstName = user.FirstName,
                LastName = user.LastName,
                Mobile = user.Mobile
            });
        }

        [HttpGet]
        [Route("api/ClaimedItems/ValidateClaim/{id}/{foundItemId}")]
        public IHttpActionResult GetValidateClaim(int id, int foundItemId)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            //if (id != claimedItemModel.Id)
            //{
            //    return BadRequest();
            //}

            var claimedItem = _db.ClaimedItems.FirstOrDefault(c => c.Id == id);

            if (claimedItem == null)
                return NotFound();

            var foundItem = _db.FoundItems.FirstOrDefault(c => c.Id == foundItemId);

            if (foundItem == null)
                return NotFound();
            
            string trakingId = $"{DateTime.Now:yyyymmddttMMssff}";

            claimedItem.TrackingId = trakingId;
            foundItem.TrackingId = trakingId;

            _db.Entry(claimedItem).State = EntityState.Modified;
            _db.Entry(foundItem).State = EntityState.Modified;

            try
            {
                _db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ClaimedItemExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return Ok(trakingId);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                _db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ClaimedItemExists(int id)
        {
            return _db.ClaimedItems.Count(e => e.Id == id) > 0;
        }
    }
}
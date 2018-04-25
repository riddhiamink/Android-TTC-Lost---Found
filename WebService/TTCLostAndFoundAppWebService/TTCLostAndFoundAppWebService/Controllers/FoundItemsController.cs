using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Http.Description;
using System.Web.Mvc;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using TTCLostAndFoundAppWebService.Models;

namespace TTCLostAndFoundAppWebService.Controllers
{
    [System.Web.Http.Authorize]
    public class FoundItemsController : ApiController
    {
        private readonly ApplicationDbContext _db = new ApplicationDbContext();
        private readonly ApplicationUserManager _userManager;

        public FoundItemsController()
        {
        }

        public FoundItemsController(ApplicationUserManager userManager)
        {
            _userManager = userManager;
        }

        public ApplicationUserManager UserManager => _userManager ?? Request.GetOwinContext().GetUserManager<ApplicationUserManager>();

        // GET: api/FoundItems
        public IEnumerable GetFoundItems()
        {
            return _db.FoundItems.OrderByDescending(c => c.DateLost).Select(item => new FoundItemBindingModel
            {
                Id = item.Id,
                UserId = item.UserId,
                DateLost = item.DateLost,
                Description = item.Description,
                Location = item.Location,
                Category = item.Category,
                Color = item.Color,
                Image = item.Image,
                TrackingId = string.IsNullOrEmpty(item.TrackingId) ? "null" : item.TrackingId
            }).ToList();
        }

        // GET: api/FoundItems/5
        [ResponseType(typeof(FoundItemBindingModel))]
        public IHttpActionResult GetFoundItem(int id)
        {
            FoundItem foundItem = _db.FoundItems.Find(id);
            if (foundItem == null)
            {
                return NotFound();
            }

            var foundModel = new FoundItemBindingModel
            {
                Category = foundItem.Category,
                Color = foundItem.Color,
                DateLost = foundItem.DateLost,
                Description = foundItem.Description,
                Location = foundItem.Location,
                TrackingId = string.IsNullOrEmpty(foundItem.TrackingId) ? "null" : foundItem.TrackingId,
                Image = foundItem.Image,
                UserId = foundItem.UserId
            };
            return Ok(foundModel);
        }

        [ResponseType(typeof(FoundItemBindingModel))]
        [System.Web.Http.HttpGet]
        public IEnumerable GetFoundItems(string query)
        {
            return _db.FoundItems.OrderByDescending(c => c.DateLost).Where(c =>
                c.Category.ToLower().Contains(query.ToLower())
                || c.TrackingId == query).Select(item => new FoundItemBindingModel
            {
                Id = item.Id,
                UserId = item.UserId,
                DateLost = item.DateLost,
                Description = item.Description,
                Location = item.Location,
                Category = item.Category,
                Color = item.Color,
                Image = item.Image,
                TrackingId = string.IsNullOrEmpty(item.TrackingId) ? "null" : item.TrackingId
            }).ToList();
        }

        // PUT: api/FoundItems/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutFoundItem(int id, FoundItemAddBindingModel foundItemModel)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            //if (id != foundItemModel.Id)
            //{
            //    return BadRequest();
            //}

            var foundItem = _db.FoundItems.FirstOrDefault(c => c.Id == id);

            if (foundItem == null)
                return NotFound();

            foundItem.Category = foundItemModel.Category;
            foundItem.Color = foundItemModel.Color;
            foundItem.DateLost = foundItemModel.DateLost;
            foundItem.Description = foundItemModel.Description;
            foundItem.Location = foundItemModel.Location;

            _db.Entry(foundItem).State = EntityState.Modified;

            try
            {
                _db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!FoundItemExists(id))
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

        // POST: api/FoundItems
        [ResponseType(typeof(FoundItemAddBindingModel))]
        public IHttpActionResult PostFoundItem()
        {
            var user = UserManager.FindByName(User.Identity.Name);
            string filePath = "";
            try
            {

                var httpRequest = HttpContext.Current.Request;
                
                HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.Created);

                if (httpRequest.Files.Count > 0)
                {
                    var postedFile = httpRequest.Files[0];

                    int MaxContentLength = 1024 * 1024 * 1; //Size = 1 MB  

                    IList<string> AllowedFileExtensions = new List<string> {  ".png" };
                    var ext = postedFile.FileName.Substring(postedFile.FileName.LastIndexOf('.'));
                    var extension = ext.ToLower();
                    if (!AllowedFileExtensions.Contains(extension))
                    {

                        var message = string.Format("Please Upload image of type .jpg,.gif,.png.");

                        ModelState.AddModelError("error", message);
                        return BadRequest(ModelState);
                    }
                    else if (postedFile.ContentLength > MaxContentLength)
                    {

                        var message = string.Format("Please Upload a file upto 1 mb.");

                        ModelState.AddModelError("error", message);
                        return BadRequest(ModelState);
                    }
                    else
                    {
                        filePath = "Images/" + postedFile.FileName + $"{DateTime.Now:ddmmyyttMMss}" + extension;
                        var fullPath = HttpContext.Current.Server.MapPath("~/" + filePath);

                        postedFile.SaveAs(fullPath);

                    }
                }
                else
                {
                    filePath = "Images/lost_item.png";
                    //    //var res = string.Format("Please Upload a image.");
                    //    //ModelState.AddModelError("error", res);
                    //    //return NotFound();
                }
                var foundItem = new FoundItem
                {
                    Category = httpRequest.Form["Category"],
                    Color = httpRequest.Form["Color"],
                    DateLost = Convert.ToDateTime(httpRequest.Form["DateLost"]),
                    Description = httpRequest.Form["Description"],
                    Location = httpRequest.Form["Location"],
                    UserId = user.Id,
                    Image = filePath
                };

                _db.FoundItems.Add(foundItem);
                _db.SaveChanges();

                return CreatedAtRoute("DefaultApi", new { id = foundItem.Id }, foundItem);
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("error", ex.Message);
                return BadRequest(ModelState);
            }
        }

        // DELETE: api/FoundItems/5
        [ResponseType(typeof(FoundItem))]
        public IHttpActionResult DeleteFoundItem(int id)
        {
            FoundItem foundItem = _db.FoundItems.Find(id);
            if (foundItem == null)
            {
                return NotFound();
            }

            _db.FoundItems.Remove(foundItem);
            _db.SaveChanges();

            return Ok(foundItem);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                _db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool FoundItemExists(int id)
        {
            return _db.FoundItems.Count(e => e.Id == id) > 0;
        }
    }
}
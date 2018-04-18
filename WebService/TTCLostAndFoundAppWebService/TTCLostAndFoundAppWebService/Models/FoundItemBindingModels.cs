using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace TTCLostAndFoundAppWebService.Models
{
    public class FoundItemBindingModel
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [Display(Name = "Id")]
        public int Id { get; set; }

        [StringLength(128, ErrorMessage = "The UserId length should be between {1} and {0}", MinimumLength = 3)]
        [Required]
        [Display(Name = "UserId")]
        public string UserId { get; set; }

        [Required]
        [StringLength(20, ErrorMessage = "The Category length should be between {1} and {0}", MinimumLength = 3)]

        [Display(Name = "Category")]
        public string Category { get; set; }

        [Required]
        [StringLength(20, ErrorMessage = "The Color length should be between {1} and {0}", MinimumLength = 3)]
        [Display(Name = "Color")]
        public string Color { get; set; }

        [Required]
        [Display(Name = "Description")]
        [StringLength(500, ErrorMessage = "The Description length should be between {1} and {0}", MinimumLength = 3)]
        public string Description { get; set; }

        [Required]
        [StringLength(100, ErrorMessage = "The Location length should be between {1} and {0}", MinimumLength = 3)]
        [Display(Name = "Location")]
        public string Location { get; set; }

        [Display(Name = "Image")]
        public string Image { get; set; }

        [Required]
        [DataType(DataType.DateTime)]
        [Display(Name = "DateLost")]
        public DateTime DateLost { get; set; }

        [StringLength(120, ErrorMessage = "The Tracking length should be between {1} and {0}", MinimumLength = 3)]
        [Display(Name = "TrakingId")]
        public string TrackingId { get; set; }
    }

    public class FoundItemAddBindingModel
    {
        [Required]
        [StringLength(20, ErrorMessage = "The Category length should be between {1} and {0}", MinimumLength = 3)]

        [Display(Name = "Category")]
        public string Category { get; set; }

        [Required]
        [StringLength(20, ErrorMessage = "The Color length should be between {1} and {0}", MinimumLength = 3)]
        [Display(Name = "Color")]
        public string Color { get; set; }

        [Required]
        [Display(Name = "Description")]
        [StringLength(500, ErrorMessage = "The Description length should be between {1} and {0}", MinimumLength = 3)]
        public string Description { get; set; }

        [Required]
        [StringLength(100, ErrorMessage = "The Location length should be between {1} and {0}", MinimumLength = 3)]
        [Display(Name = "Location")]
        public string Location { get; set; }
        
        [Required]
        [DataType(DataType.DateTime)]
        [Display(Name = "DateLost")]
        public DateTime DateLost { get; set; }
        
    }
}
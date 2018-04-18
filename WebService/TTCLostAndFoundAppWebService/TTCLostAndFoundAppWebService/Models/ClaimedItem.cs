using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace TTCLostAndFoundAppWebService.Models
{
    public class ClaimedItem
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        
        [StringLength(128)]
        [Required]
        public string UserId { get; set; }

        [Required]
        [StringLength(20)]
        public string Category { get; set; }

        [Required]
        [StringLength(20)]
        public string Color { get; set; }

        [Required]
        [StringLength(500)]
        public string Description { get; set; }

        [Required]
        [StringLength(100)]
        public string Location { get; set; }

        [Required]
        [DataType(DataType.DateTime)]
        public DateTime DateLost { get; set; }

        [StringLength(120)]
        public string TrackingId { get; set; }
       
        [ForeignKey(nameof(UserId))]
        public ApplicationUser User { get; set; }

    }
}
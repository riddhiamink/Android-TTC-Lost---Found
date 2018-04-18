using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace TTCLostAndFoundAppWebService.Models
{
    public class IdVerification
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set ; }

        [StringLength(50)]
        [Required]
        public string Name { get; set; }
    }
}
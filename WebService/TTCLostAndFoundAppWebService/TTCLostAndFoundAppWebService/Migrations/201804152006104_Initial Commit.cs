namespace TTCLostAndFoundAppWebService.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class InitialCommit : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.ClaimedItems",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        UserId = c.String(nullable: false, maxLength: 128),
                        Category = c.String(nullable: false, maxLength: 20),
                        Color = c.String(nullable: false, maxLength: 20),
                        Description = c.String(nullable: false, maxLength: 500),
                        Location = c.String(nullable: false, maxLength: 100),
                        DateLost = c.DateTime(nullable: false),
                        TrackingId = c.String(maxLength: 120),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.AspNetUsers", t => t.UserId, cascadeDelete: true)
                .Index(t => t.UserId);
            
            CreateTable(
                "dbo.FoundItems",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        UserId = c.String(maxLength: 128),
                        Category = c.String(nullable: false, maxLength: 20),
                        Color = c.String(nullable: false, maxLength: 20),
                        Description = c.String(nullable: false, maxLength: 500),
                        Location = c.String(nullable: false, maxLength: 100),
                        DateLost = c.DateTime(nullable: false),
                        Image = c.String(maxLength: 120),
                        TrackingId = c.String(maxLength: 120),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.AspNetUsers", t => t.UserId)
                .Index(t => t.UserId);
            
            CreateTable(
                "dbo.IdVerifications",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(nullable: false, maxLength: 50),
                    })
                .PrimaryKey(t => t.Id);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.FoundItems", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.ClaimedItems", "UserId", "dbo.AspNetUsers");
            DropIndex("dbo.FoundItems", new[] { "UserId" });
            DropIndex("dbo.ClaimedItems", new[] { "UserId" });
            DropTable("dbo.IdVerifications");
            DropTable("dbo.FoundItems");
            DropTable("dbo.ClaimedItems");
        }
    }
}

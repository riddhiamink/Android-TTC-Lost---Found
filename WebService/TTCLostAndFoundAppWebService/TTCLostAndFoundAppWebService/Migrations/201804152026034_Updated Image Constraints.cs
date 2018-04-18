namespace TTCLostAndFoundAppWebService.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class UpdatedImageConstraints : DbMigration
    {
        public override void Up()
        {
            AlterColumn("dbo.FoundItems", "Image", c => c.String());
        }
        
        public override void Down()
        {
            AlterColumn("dbo.FoundItems", "Image", c => c.String(maxLength: 120));
        }
    }
}

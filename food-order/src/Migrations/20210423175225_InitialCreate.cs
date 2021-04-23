using System.IO;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

namespace food_order.Migrations
{
    public partial class InitialCreate : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            // migrationBuilder.Sql(File.ReadAllText("./Migrations/20210423175225_InitialCreate.sql"));
            migrationBuilder.CreateTable(
                name: "db_order",
                columns: table => new
                {
                    id = table.Column<long>(type: "int", nullable: false)
                        .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn),
                    uuid = table.Column<string>(type: "binary(36)", nullable: true),
                    restaurant_uuid = table.Column<string>(type: "binary(36)", nullable: true),
                    total = table.Column<decimal>(type: "decimal(10,2)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_db_order", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "db_order_item",
                columns: table => new
                {
                    id = table.Column<long>(type: "int", nullable: false)
                        .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn),
                    uuid = table.Column<string>(type: "binary(36)", nullable: true),
                    name = table.Column<string>(type: "varchar(150)", nullable: true),
                    amount = table.Column<int>(type: "int", nullable: false),
                    unit_value = table.Column<decimal>(type: "decimal(10,2)", nullable: false),
                    order_id = table.Column<long>(type: "int", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_db_order_item", x => x.id);
                    table.ForeignKey(
                        name: "FK_order_item_order_order_id",
                        column: x => x.order_id,
                        principalTable: "db_order",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateIndex(
                name: "IX_db_order_item_order_id",
                table: "db_order_item",
                column: "order_id");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "db_order_item");

            migrationBuilder.DropTable(
                name: "db_order");
        }
    }
}

using food_order.Gateway.Database.Model;
using Microsoft.EntityFrameworkCore;

namespace food_order.Gateway.Database.Data
{
    public class OrderContext : DbContext
    {
        public OrderContext(DbContextOptions<OrderContext> options) : base(options)
        {
        }
        
        public DbSet<OrderModel> OrderModel { get; set; }
        public DbSet<OrderItemModel> OrderItemModel { get; set; }
        
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<OrderModel>().ToTable("db_order")
                .HasKey(orderModel => new { orderModel.Id });
            modelBuilder.Entity<OrderModel>()
                .HasMany(orderModel => orderModel.Items)
                .WithOne()
                .HasForeignKey(orderItemModel => orderItemModel.OrderId);
            modelBuilder.Entity<OrderModel>()
                .Navigation(orderModel => orderModel.Items)
                .UsePropertyAccessMode(PropertyAccessMode.Property);
            
            modelBuilder.Entity<OrderItemModel>().ToTable("db_order_item")
                .HasKey(orderItemModel => new { orderItemModel.Id });
            modelBuilder.Entity<OrderItemModel>()
                .Property(orderItemModel => orderItemModel.OrderId).HasColumnName("order_id");
            modelBuilder.Entity<OrderItemModel>()
                .Property(orderItemModel => orderItemModel.UnitValue).HasColumnName("unit_value");
        }
    }
}
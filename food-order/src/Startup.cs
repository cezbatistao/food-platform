using System.Text.Json.Serialization;
using FluentValidation.AspNetCore;
using food_order.Entrypoint.Rest;
using food_order.Entrypoint.Rest.Json;
using food_order.Gateway;
using food_order.Gateway.Database;
using food_order.Gateway.Database.Data;
using food_order.Gateway.Http;
using food_order.UseCase;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.OpenApi.Models;
using MySqlConnector;
using IHostingEnvironment = Microsoft.AspNetCore.Hosting.IHostingEnvironment;

namespace food_order
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddAutoMapper(typeof(Startup));
            
            string mySqlConnectionStr = Configuration.GetConnectionString("Default");
            services.AddTransient<MySqlConnection>(_ => new MySqlConnection(mySqlConnectionStr));
            services.AddDbContext<OrderContext>(options =>
                options.UseMySql(Configuration.GetConnectionString("Default"), ServerVersion.AutoDetect(mySqlConnectionStr)));
            
            services.AddHttpClient();
            
            services.Configure<ApiBehaviorOptions>(opt => { opt.SuppressModelStateInvalidFilter = true; });
            services.AddControllers()
                .AddFluentValidation(fv => 
                    fv.RegisterValidatorsFromAssemblyContaining<OrderRequestValidator>())
                .AddJsonOptions(options =>
                {
                    options.JsonSerializerOptions.DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull;
                });
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "food-order", Version = "1.0.0-0 " });
            });

            services.AddScoped<RegisterOrder, RegisterOrder>();
            services.AddScoped<IOrderGateway, OrderGatewayImpl>();
            services.AddScoped<IRestaurantGateway, RestaurantGatewayImpl>();
            services.AddScoped<RestaurantClient, RestaurantClient>();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment() || env.IsEnvironment("dce"))
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "food_order v1"));
            }
            else
            {
                app.UseHttpsRedirection();
            }

            if (env.IsEnvironment("dce"))
            {
                UpdateDatabase(app);
            }

            app.UseRouting();
            
            app.UseMiddleware(typeof(ExceptionHandlingMiddleware));

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
        
        private static void UpdateDatabase(IApplicationBuilder app)
        {
            using (var serviceScope = app.ApplicationServices
                .GetRequiredService<IServiceScopeFactory>()
                .CreateScope())
            {
                using (var context = serviceScope.ServiceProvider.GetService<OrderContext>())
                {
                    context.Database.Migrate();
                }
            }
        }
    }
}

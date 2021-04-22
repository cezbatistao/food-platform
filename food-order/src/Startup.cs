using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using food_order.Entrypoint.Rest;
using food_order.Gateway;
using food_order.Gateway.Database;
using food_order.Gateway.Http;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.OpenApi.Models;

using food_order.UseCase;

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

            services.AddControllers();
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "food-order", Version = "1.0.0-0 " });
            });

            services.AddScoped<RegisterOrder, RegisterOrder>();
            services.AddScoped<IOrderGateway, OrderGatewayImpl>();
            services.AddScoped<IRestaurantGateway, RestaurantGatewayImpl>();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "food_order v1"));
            }
            else
            {
                app.UseHttpsRedirection();
            }

            app.UseRouting();
            
            app.UseMiddleware(typeof(ExceptionHandlingMiddleware));

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}

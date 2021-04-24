using System;
using AutoFixture;
using AutoMapper;
using DotNet.Testcontainers.Containers.Builders;
using DotNet.Testcontainers.Containers.Configurations.Databases;
using DotNet.Testcontainers.Containers.Modules.Databases;
using DotNet.Testcontainers.Images;
using food_order.Gateway.Database;
using food_order.Gateway.Database.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;

namespace test.Support
{
    public class DatabaseFixture : IDisposable
    {
        private MySqlTestcontainer _mySqlTestcontainer;
        public OrderContext OrderContext { get; private set; }
        public IMapper Mapper { get; private set; }

        public DatabaseFixture()
        {
            _mySqlTestcontainer = CreateMySqlContainer();

            var serviceProvider = new ServiceCollection()
                .AddEntityFrameworkMySql()
                .BuildServiceProvider();

            var builder = new DbContextOptionsBuilder<OrderContext>();

            var urlConnection = $"server={_mySqlTestcontainer.Hostname};port={_mySqlTestcontainer.Port};" +
                                $"user={_mySqlTestcontainer.Username};password={_mySqlTestcontainer.Password};" +
                                $"database={_mySqlTestcontainer.Database}";
            builder.UseMySql(urlConnection, ServerVersion.AutoDetect(urlConnection))
                .UseInternalServiceProvider(serviceProvider);

            OrderContext = new OrderContext(builder.Options);
            OrderContext.Database.Migrate();

            var mockMapper = new MapperConfiguration(cfg =>
            {
                cfg.AddProfile(new OrderProfile());
            });
            Mapper = mockMapper.CreateMapper();
        }

        public MySqlTestcontainer CreateMySqlContainer()
        {
            var mySqlTestcontainer = new TestcontainersBuilder<MySqlTestcontainer>()
                .WithDatabase(new MySqlTestcontainerConfiguration("mysql:5.7")
                {
                    Database = "db_order_it", 
                    Username = "order_user_it", 
                    Password = "order_passwd_it"
                })
                .WithExposedPort(3306)
                .Build();
            
            mySqlTestcontainer.StartAsync().ConfigureAwait(false).GetAwaiter().GetResult();

            return mySqlTestcontainer;
        }
        
        public void Dispose()
        {
            _mySqlTestcontainer.DisposeAsync();
        }
    }
}

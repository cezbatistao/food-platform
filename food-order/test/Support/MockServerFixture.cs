using System;
using AutoMapper;
using DotNet.Testcontainers.Containers.Builders;
using DotNet.Testcontainers.Containers.Modules;
using DotNet.Testcontainers.Containers.WaitStrategies;
using DotNet.Testcontainers.Images;
using DotNet.Testcontainers.Images.Builders;
using food_order.Gateway.Database.Data;
using food_order.Gateway.Http;

namespace test.Support
{
    public class MockServerFixture : IDisposable
    {
        private TestcontainersContainer _container;
        
        public IMapper Mapper { get; private set; }
        public string Hostname { get; private set; }
        public ushort Port { get; private set; }

        public MockServerFixture()
        {
            _container = CreateMySqlContainer();

            Hostname = _container.Hostname;
            Port = _container.GetMappedPublicPort("8882");
            var mockMapper = new MapperConfiguration(cfg =>
            {
                cfg.AddProfile(new OrderProfile());
            });
            Mapper = mockMapper.CreateMapper();
        }

        public TestcontainersContainer CreateMySqlContainer()
        {
            var container = new TestcontainersBuilder<TestcontainersContainer>()
                .WithImage("node:6.11-slim")
                .WithWorkingDirectory("/mocks")
                .WithMount("../../../../dependencies/stubby/", "/mocks")
                .WithEntrypoint("/bin/sh", "-c", "npm install -g stubby@4.1.1 && stubby -d /mocks/init.yml -s 8882 -a 8889 -w")
                .WithCleanUp(false)
                .WithExposedPort("8882")
                .WithCleanUp(true)
                .WithWaitStrategy(Wait.ForUnixContainer().UntilPortIsAvailable(8882))
                .WithPortBinding("8882", true)
                .Build();

            container.StartAsync().ConfigureAwait(false).GetAwaiter().GetResult();

            return container;
        }
        
        public void Dispose()
        {
            _container.DisposeAsync();
        }
    }
}

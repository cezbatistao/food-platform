using AutoFixture;
using test.Fixture.Templates;

namespace test.Fixture
{
    public class GatewayIntegrationTestCustomization : ICustomization
    {
        public void Customize(IFixture fixture)
        {
            fixture.Register(() => { return OrderTemplate.GetWithTwoItems(); });
        }
    }
}

using AutoFixture;
using test.Fixture.Templates;

namespace test.Fixture
{
    public class IntegrationTestCustomization : ICustomization
    {
        public void Customize(IFixture fixture)
        {
            fixture.Register(() => { return OrderTemplate.GetWithSingleItem(); });
            fixture.Register(() => { return OrderRequestTemplate.Get(); });
        }
    }
}

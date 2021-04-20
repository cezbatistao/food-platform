namespace food_order.Domain.Exception
{
    public class EntityNotFoundException : BaseException
    {
        public EntityNotFoundException(string code, string error,
            string description) : base(code, error, description) { }
    }
}

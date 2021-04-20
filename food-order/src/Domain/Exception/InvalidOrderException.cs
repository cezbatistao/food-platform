namespace food_order.Domain.Exception
{
    public class InvalidOrderException : BaseException
    {
        public InvalidOrderException(string code, string error,
            string description) : base(code, error, description) { }
    }
}

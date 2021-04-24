namespace food_order.Domain.Exception
{
    public class BaseException : System.Exception
    {
        public string Code { get; }
        public string Error { get; }

        public BaseException(string code, string error, 
            string description) : base(description)
        {
            this.Code = code;
            this.Error = error;
        }
        
        public BaseException(string code, string error, 
            string description, System.Exception exception) : base(description, exception)
        {
            this.Code = code;
            this.Error = error;
        }
    }
}

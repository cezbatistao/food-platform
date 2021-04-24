using food_order.Domain.Exception;
using food_order.Gateway.Http.Json.Error;

namespace food_order.Gateway.Http.Exception
{
    public class RequestRestApiException : BaseException
    {
        public ErrorDetailResponse ErrorDetail { get;  }

        public RequestRestApiException(string code, string error,
            string description, ErrorDetailResponse errorDetail) : base(code, error, description)
        {
            ErrorDetail = errorDetail;
        }

        public RequestRestApiException(string code, string error, string description, 
            System.Exception exception) : base(code, error, description, exception) { }
    }
}

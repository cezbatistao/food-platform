using food_order.Entrypoint.Rest.Json.Error;

namespace food_order.Entrypoint.Rest.Json
{
    public class ErrorResponse
    {
        public ErrorDetailResponse Error { get; set; }

        public ErrorResponse(ErrorDetailResponse error)
        {
            this.Error = error;
        }
    }
}

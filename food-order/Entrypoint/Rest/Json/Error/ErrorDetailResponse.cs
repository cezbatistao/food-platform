namespace food_order.Entrypoint.Rest.Json.Error
{
    public class ErrorDetailResponse
    {
        public string code { get; set; }
        public string message { get; set; }

        public ErrorDetailResponse(string code, string message)
        {
            this.code = code;
            this.message = message;
        }
    }
}

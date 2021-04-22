using System.Text.Json.Serialization;

namespace food_order.Entrypoint.Rest.Json
{
    public class DataResponse<T>
    {
        [JsonPropertyName("data")]
        public T Data { get; set; }

        public DataResponse(T data)
        {
            this.Data = data;
        }
    }
}
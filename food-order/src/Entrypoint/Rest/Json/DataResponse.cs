using Newtonsoft.Json;

namespace food_order.Entrypoint.Rest.Json
{
    public class DataResponse<T>
    {
        [JsonProperty("data")]
        public T Data { get; set; }

        public DataResponse(T data)
        {
            this.Data = data;
        }
    }
}

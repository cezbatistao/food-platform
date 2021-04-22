using System.Collections.Generic;
using Newtonsoft.Json;

namespace food_order.Entrypoint.Rest.Json.Error
{
    public class ErrorDetailResponse
    {
        public string Code { get; set; }
        public string Message { get; set; }
        
        [JsonProperty("field_errors", NullValueHandling = NullValueHandling.Ignore)]
        public List<FieldErrorResponse> FieldErrors { get; set; }

        public ErrorDetailResponse(string code, string message, List<FieldErrorResponse> fieldErrors)
        {
            this.Code = code;
            this.Message = message;
            this.FieldErrors = fieldErrors;
        }
    }
}

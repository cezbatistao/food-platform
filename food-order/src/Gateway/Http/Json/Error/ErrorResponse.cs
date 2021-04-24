using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace food_order.Gateway.Http.Json.Error
{
    public class ErrorDetailResponse
    {
        [JsonPropertyName("code")]
        public string Code { get; set; }
        
        [JsonPropertyName("message")]
        public string Message { get; set; }
        
        [JsonPropertyName("field_errors")]
        public List<FieldErrorResponse> FieldErrors { get; set; }

        public ErrorDetailResponse(string code, string message, List<FieldErrorResponse> fieldErrors)
        {
            this.Code = code;
            this.Message = message;
            this.FieldErrors = fieldErrors;
        }

        protected ErrorDetailResponse() { }
    }
    
    public class FieldErrorResponse
    {
        [JsonPropertyName("field")]
        public string Field { get; set; }
        
        [JsonPropertyName("rrrors")]
        public List<string> Errors { get; set; }

        public FieldErrorResponse(string field, List<string> errors)
        {
            Field = field;
            Errors = errors;
        }
        
        protected FieldErrorResponse() { }
    }
}

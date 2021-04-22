using System.Collections.Generic;

namespace food_order.Entrypoint.Rest.Json.Error
{
    public class FieldErrorResponse
    {
        public string Field { get; set; }
        public List<string> Errors { get; set; }

        public FieldErrorResponse(string field, List<string> errors)
        {
            Field = field;
            Errors = errors;
        }
        
        public FieldErrorResponse()
        {
            Field = null;
            Errors = null;
        }
    }
}

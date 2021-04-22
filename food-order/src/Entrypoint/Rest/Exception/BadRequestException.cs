using System;
using System.Collections.Generic;
using System.Linq;
using food_order.Domain.Exception;
using Microsoft.AspNetCore.Mvc.ModelBinding;

namespace food_order.Entrypoint.Rest.Exception
{
    public class BadRequestException : BaseException
    {
        public List<FieldError> FieldErrors { get; }
        
        public BadRequestException(string code, string error,
            string description, ModelStateDictionary modelState) : base(code, error, description)
        {
            FieldErrors = modelState.Select(keyValuePair => 
                new FieldError(keyValuePair.Key, keyValuePair.Value.Errors.Select(modelError => 
                    modelError.ErrorMessage).ToList())).ToList();
        }
    }

    public class FieldError
    {
        public string Field;
        public List<string> Errors;

        public FieldError(string field, List<string> errors)
        {
            Field = field;
            Errors = errors;
        }
    }
}

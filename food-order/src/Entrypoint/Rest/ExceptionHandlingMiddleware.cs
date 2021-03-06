using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using food_order.Domain.Exception;
using food_order.Entrypoint.Rest.Exception;
using food_order.Entrypoint.Rest.Json;
using food_order.Entrypoint.Rest.Json.Error;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace food_order.Entrypoint.Rest
{
    public class ExceptionHandlingMiddleware
    {
        private readonly RequestDelegate _next;
        private readonly ILogger _logger;

        public ExceptionHandlingMiddleware(RequestDelegate next, 
            ILogger<ExceptionHandlingMiddleware> logger)
        {
            this._next = next;
            this._logger = logger;
        }
        
        public async Task Invoke(HttpContext context)
        {
            try
            {
                await _next(context);
            }
            catch (System.Exception ex)
            {
                _logger.LogError($"Error handler: {ex}", ex);
                await HandleExceptionAsync(context, ex);
            }
        }
        
        private static Task HandleExceptionAsync(HttpContext context, System.Exception exception)
        {
            var status = HttpStatusCode.InternalServerError; // 500 if unexpected
            var code = "9999";
            var message = exception.Message;
            List<FieldErrorResponse> fieldErrorsResponse = null;

            if (exception is BaseException)
            {
                var baseException = (BaseException)exception;
                
                if(exception is BadRequestException)
                {
                    status = HttpStatusCode.BadRequest;
                    fieldErrorsResponse = ((BadRequestException)exception).FieldErrors.Select(error => 
                        new FieldErrorResponse(error.Field, error.Errors)).ToList();
                }
                else if (exception is EntityNotFoundException) status = HttpStatusCode.NotFound;
                else if (exception is InvalidOrderException)   status = HttpStatusCode.UnprocessableEntity;

                code = baseException.Code;
                message = exception.Message;
            }
            
            DefaultContractResolver contractResolver = new DefaultContractResolver
            {
                NamingStrategy = new SnakeCaseNamingStrategy()
            };
            
            var jsonErrorResponse = JsonConvert.SerializeObject(
                new ErrorResponse(new ErrorDetailResponse(code, message, fieldErrorsResponse)), 
                new JsonSerializerSettings
                {
                    ContractResolver = contractResolver,
                    Formatting = Formatting.Indented
                });
            
            context.Response.ContentType = "application/json";
            context.Response.StatusCode = (int)status;
            return context.Response.WriteAsync(jsonErrorResponse);
        }
    }
}

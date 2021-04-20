using System;
using System.Net;
using System.Threading.Tasks;
using food_order.Domain.Exception;
using food_order.Entrypoint.Rest.Json.Error;
using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;

namespace food_order.Entrypoint.Rest
{
    public class ExceptionHandlingMiddleware
    {
        private readonly RequestDelegate _next;

        public ExceptionHandlingMiddleware(RequestDelegate next)
        {
            this._next = next;
        }
        
        public async Task Invoke(HttpContext context)
        {
            try
            {
                await _next(context);
            }
            catch (Exception ex)
            {
                await HandleExceptionAsync(context, ex);
            }
        }
        
        private static Task HandleExceptionAsync(HttpContext context, Exception exception)
        {
            var status = HttpStatusCode.InternalServerError; // 500 if unexpected
            var code = "9999";
            var message = exception.Message;

            if (exception is BaseException)
            {
                var baseException = (BaseException)exception;
                
                if      (exception is EntityNotFoundException) status = HttpStatusCode.NotFound;
                else if (exception is InvalidOrderException)   status = HttpStatusCode.UnprocessableEntity;

                code = baseException.Code;
                message = exception.Message;
            }
            
            var result = JsonConvert.SerializeObject(new ErrorDetailResponse(code, message));
            context.Response.ContentType = "application/json";
            context.Response.StatusCode = (int)status;
            return context.Response.WriteAsync(result);
        }
    }
}
FROM mcr.microsoft.com/dotnet/aspnet:5.0-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY out .

ENV ENVIRONMENT_APP Development
ENV ENVIRONMENT_PORT 5000

ENV ASPNETCORE_ENVIRONMENT=${ENVIRONMENT_APP}
ENV ASPNETCORE_URLS=http://+:${ENVIRONMENT_PORT}

EXPOSE 5000

ENTRYPOINT ["dotnet", "food-order.dll"]

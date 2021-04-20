FROM mcr.microsoft.com/dotnet/aspnet:5.0-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY out .

ENTRYPOINT ["dotnet", "food-order.dll"]

FROM mcr.microsoft.com/dotnet/aspnet:5.0-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY out .

ARG ENVIRONMENT_PORT 5000
ARG ENVIRONMENT_APP Development

ENV ASPNETCORE_ENVIRONMENT=${ENVIRONMENT_APP}
ENV ASPNETCORE_URLS=http://+:${ENVIRONMENT_PORT}

EXPOSE ${ENVIRONMENT_PORT}

ENTRYPOINT ["dotnet", "food-order.dll"]

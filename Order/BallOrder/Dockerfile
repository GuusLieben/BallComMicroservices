#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:5.0-buster-slim AS base
WORKDIR /app

FROM mcr.microsoft.com/dotnet/sdk:5.0-buster-slim AS build
WORKDIR /src
COPY ["BallOrder/BallOrder.csproj", "BallOrder/"]
COPY ["BallOrderDomain/BallOrderDomain.csproj", "BallOrderDomain/"]
COPY ["BallOrderInfrastructure/BallOrderInfrastructure.csproj", "BallOrderInfrastructure/"]
RUN dotnet restore "BallOrder/BallOrder.csproj"
COPY . .
WORKDIR "/src/BallOrder"
RUN dotnet build "BallOrder.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "BallOrder.csproj" -c Release -o /app/publish

# Expose ports
EXPOSE 8084/tcp
ENV ASPNETCORE_URLS http://*:8084

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "BallOrder.dll"]
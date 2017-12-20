
# Project Name
DataStore

# Technology stack
Spring Boot
Undertow
GraphQL
Swagger
Spring Validation (Valang)
Spring Cache (ehcache)
Spring Cloud
Hystrix
Eureka Client
HTTPS TLSv1.2

# Company Website
https://github.com/AbrahamAlarcon/datastore

# https://www.wunderground.com/weather/api
Key ID
066631e4c9e41f5a


# Test
$ curl https://localhost:8000/datastore.gql -d '{"query": "query{pull(request:{conditions:true geolookup:true forecast:true city:\"Sydney\" country:\"Australia\"}){error{code message status} response{version features{conditions}} location{country state city lat lon requesturl}}}"}' -H 'Content-Type: application/json' -k
{"data":{"pull":{"error":null,"response":{"version":"0.1","features":{"conditions":1}},"location":{"country":"AU","state":"NSW","city":"Sydney","lat":"-33.86999893","lon":"151.21000671","requesturl":"global/stations/94768.html"}}}}

$ curl https://localhost:8000/datastore.gql -d '{"query": "query{pull(request:{conditions:true geolookup:true forecast:true city:\"Santiago\" country:\"Chile\"}){error{code message status} response{version features{conditions}} location{country state city lat lon requesturl}}}"}' -H 'Content-Type: application/json' -k
{"data":{"pull":{"error":null,"response":{"version":"0.1","features":{"conditions":1}},"location":{"country":"CH","state":"RM","city":"Santiago","lat":"-33.45999908","lon":"-70.63999939","requesturl":"global/stations/85577.html"}}}}


# Test WUnderground directly
$ curl http://api.wunderground.com/api/*****/geolookup/conditions/forecast/q/Chile/Santiago.json

{
  "response": {
  "version":"0.1",
  "termsofService":"http://www.wunderground.com/weather/api/d/terms.html",
  "features": {
  "geolookup": 1
  ,
  "conditions": 1
  ,
  "forecast": 1
  }
        }
                ,       "location": {
                "type":"INTLCITY",
                "country":"CH",
                "country_iso3166":"CL",
                "country_name":"Chile",
                "state":"RM",
                "city":"Santiago",
                "tz_short":"CLST",
                "tz_long":"America/Santiago",
...

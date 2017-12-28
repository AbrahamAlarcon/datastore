
# Project Name
DataStore
The idea of this project is to show how a simple RESTful service can be transformed in a data store service,
allowing the service client to decide what data wants back.
Here I am just showing the potential of using frameworks such as GraphQL, which would help to develop a new generation
of services based on Datasets and no only on Resources.

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

# Test this service online
$ curl -i https://datastore.cfapps.io/datastore/swagger.json
{"swagger":"2.0","info":{"description":"Privately accessible RESTful for showing data store based design for APIs.","version":"1.0","title":"DataStore Abraham.Alarcon-Org","contact":{"name":"abraham.alarcon@gmail.com"},"license":{"name":"Abraham Alarcon DataStore 1.0","url":""}},"basePath":"/datastore","tags":[{"name":"v1"}],"paths":{"/v1":{"post":{"tags":["v1"],"summary":"datastore","description":"Datastore","operationId":"pull","parameters":[{"in":"body","name":"body","description":"Datastore Request","required":true,"schema":{"$ref":"#/definitions/DatastoreRequest"}}],"responses":{"200":{"description":"successful operation","schema":{"$ref":"#/definitions/DatastoreResponse"}}}}}},"definitions":{"Response":{"type":"object","properties":{"version":{"type":"string"},"termsofService":{"type":"string"},"features":{"$ref":"#/definitions/Features"}}},"Forecast":{"type":"object","properties":{"txt_forecast":{"$ref":"#/definitions/TxtForecast"}}},"BaseError":{"type":"object","properties":{"code":{"type":"string"},"message":{"type":"string"},"fieldErrors":{"type":"object","additionalProperties":{"type":"string"}}}},"Features":{"type":"object","properties":{"geolookup":{"type":"integer","format":"int32"},"conditions":{"type":"integer","format":"int32"},"forecast":{"type":"integer","format":"int32"}}},"DatastoreResponse":{"type":"object","properties":{"response":{"$ref":"#/definitions/Response"},"location":{"$ref":"#/definitions/Location"},"forecast":{"$ref":"#/definitions/Forecast"},"error":{"$ref":"#/definitions/BaseError"}}},"ForecastDay":{"type":"object","properties":{"period":{"type":"integer","format":"int32"},"icon":{"type":"string"},"icon_url":{"type":"string"},"title":{"type":"string"},"fcttext":{"type":"string"},"fcttext_metric":{"type":"string"},"pop":{"type":"string"}}},"TxtForecast":{"type":"object","properties":{"date":{"type":"string"},"forecastday":{"type":"array","items":{"$ref":"#/definitions/ForecastDay"}}}},"DatastoreRequest":{"type":"object","properties":{"conditions":{"type":"boolean","default":false},"forecast":{"type":"boolean","default":false},"geolookup":{"type":"boolean","default":false},"country":{"type":"string"},"city":{"type":"string"}}},"Location":{"type":"object","properties":{"type":{"type":"string"},"country":{"type":"string"},"country_iso3166":{"type":"string"},"country_name":{"type":"string"},"state":{"type":"string"},"city":{"type":"string"},"tz_short":{"type":"string"},"tz_long":{"type":"string"},"lat":{"type":"string"},"lon":{"type":"string"},"zip":{"type":"string"},"magic":{"type":"string"},"wmo":{"type":"string"},"l":{"type":"string"},"requesturl":{"type":"string"},"wuiurl":{"type":"string"}}}}}

$ curl -i https://datastore.cfapps.io/datastore.gql -d '{"query": "query{pull(request:{conditions:true geolookup:true forecast:true city:\"Santiago\" country:\"Chile\"}){error{code message status} response{version features{conditions}} location{country state city lat lon requesturl}}}"}' -H 'Content-Type: application/json'
{"data":{"pull":{"error":null,"response":{"version":"0.1","features":{"conditions":1}},"location":{"country":"CH","state":"RM","city":"Santiago","lat":"-33.45999908","lon":"-70.63999939","requesturl":"global/stations/85577.html"}}}}

$ curl -i https://datastore.cfapps.io/datastore.giql

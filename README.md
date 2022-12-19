**Major Dependencies** 

* scala 2.13
* sbt
* http4s
* circe
* cats effects

**Instructions**

```bash
sbt run
```

**Api Requests**

Issue request with latitude and longitude as a query param to receive general forecast
```bash
localhost:8080/weather?latitude=30.3087&longitude=-97.6981
```
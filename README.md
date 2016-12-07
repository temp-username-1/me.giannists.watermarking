# Watermarking API

## How to run

  - first clone the current github project
  - cd to the project's directory
  - type `mvn spring-boot:run`

### Documentation
Upon initiating the project, you can navigate to `localhost:8080/swagger-ui.html` and perform the supported actions. 

For a quick overview, keep reeding.

#### POST /documents

To create/insert a new document :
```sh
$ curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
  "author": "van der Aalst",
  "title": "Process Mining",
  "type": "BOOK"
}' 'http://localhost:8080/documents'
```

will return the following response:
```
{
  "ticket": 2,
  "type": "BOOK",
  "title": "Process Mining",
  "author": "van der Aalst"
}
```

#### GET /documents/{id}
To retrieve a document by using the ticket number:
```sh
$ curl -X GET --header 'Accept: application/json' 'http://localhost:8080/documents/2'
```
will return 2 types of responses:

##### a) if the document is non watermarked: 

```sh
{
  "ticket": 2,
  "pendingWatermark": true
}
```

##### b) if the document is watermarked: 

```sh
  {
  "ticket": 2,
  "type": "BOOK",
  "title": "Process Mining",
  "author": "van der Aalst",
  "watermark": {
    "content": "literature book",
    "title": "Process Mining",
    "author": "van der Aalst",
    "topic": "SCIENCE"
  }
}
```

#### POST /documents/{id}/watermarks
To post a new watermark for a document:
```sh
$ curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
  "content": "literature book",
  "title": "Process Mining",
  "author": "van der Aalst",
  "topic": "SCIENCE"
}' 'http://localhost:8080/documents/2/watermarks'
```

will return :
```sh
{
  "content": "literature book",
  "title": "Process Mining",
  "author": "van der Aalst",
  "topic": "SCIENCE"
}
```

[![Build Status](https://github.com/danieletorelli/loan-cashflows-api/workflows/Scala%20CI/badge.svg?branch=master)](https://github.com/danieletorelli/loan-cashflows-api/actions?query=workflow%3A%22Scala+CI%22+branch%3Amaster)
[![codecov](https://codecov.io/gh/danieletorelli/loan-cashflows-api/branch/master/graph/badge.svg)](https://codecov.io/gh/danieletorelli/loan-cashflows-api)
[![Scala Steward Badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)




Challenge
=================

Refer to [CHALLENGE.md](CHALLENGE.md) for the challenge description.

Run
---

The main class is: `it.mdtorelli.cashflows.Main`.

```sh
sbt run
```

On run, the server will bind at: `http://0.0.0.0:8080`.

Docker
------

```sh
sbt docker:publishLocal

docker run -p 8080:8080 --rm loan-cashflows-api
```

Endpoints
---------

- `GET /`: Welcome page
- `POST /cashflows`: Cashflow ingestion

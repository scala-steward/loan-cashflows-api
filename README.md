[![build](https://img.shields.io/github/actions/workflow/status/danieletorelli/loan-cashflows-api/scala.yml?branch=master&style=for-the-badge)](https://github.com/danieletorelli/loan-cashflows-api/actions?query=workflow%3A%22Scala+CI%22+branch%3Amaster)
[![Scala Steward](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=for-the-badge)](https://scala-steward.org)
[![release](https://img.shields.io/github/v/release/danieletorelli/loan-cashflows-api?style=for-the-badge)](https://github.com/danieletorelli/loan-cashflows-api/releases/latest)
[![license](https://img.shields.io/github/license/danieletorelli/loan-cashflows-api?style=for-the-badge)](https://github.com/danieletorelli/loan-cashflows-api/blob/master/LICENSE.md)

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

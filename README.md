[![Build Status](https://api.travis-ci.org/danieletorelli/loan-cashflows-api.png?branch=master)](https://travis-ci.org/danieletorelli/loan-cashflows-api)


Challenge
=================

Refer to [CHALLENGE.md](CHALLENGE.md) for the challenge description.

Run
---

The main class is: `it.mdtorelli.cashflows.Main`.

```sh
sbt run
```

On run, the server will reply to `http://0.0.0.0:8080`.

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

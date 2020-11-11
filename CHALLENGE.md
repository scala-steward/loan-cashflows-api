Coding Challenge
================

A financial institute wants to develop a public HTTP based API that calculate ​APR​ (Annual Percentage Rate) and ​IRR [^1] (Internal Rate Of Return) for a given investment/loan.

[^1]: 1 Not XIRR or MIRR

Requirements
------------

Implement an HTTP API that exposes an endpoint that consumes given cash flows in the form of Json and returns a Json response containing computed APR and IRR.

### Input

```json
{
  "principal": 51020400,
  "upfrontFee": {
    "value": 1020400
  },
  "upfrontCreditlineFee": {
    "value": 0
  },
  "schedule": [
    {
      "id": 1,
      "date": "2016-10-20",
      "principal": 3595000,
      "interestFee": 1530600
    },
    {
      "id": 2,
      "date": "2016-11-21",
      "principal": 3702800,
      "interestFee": 1422800
    },
    {
      "id": 3,
      "date": "2016-12-20",
      "principal": 3813900,
      "interestFee": 1311700
    },
    {
      "id": 4,
      "date": "2017-01-20",
      "principal": 3928300,
      "interestFee": 1197300
    },
    {
      "id": 5,
      "date": "2017-02-20",
      "principal": 4046200,
      "interestFee": 1079400
    },
    {
      "id": 6,
      "date": "2017-03-20",
      "principal": 4167600,
      "interestFee": 958000
    },
    {
      "id": 7,
      "date": "2017-04-20",
      "principal": 4292600,
      "interestFee": 833000
    },
    {
      "id": 8,
      "date": "2017-05-22",
      "principal": 4421400,
      "interestFee": 704200
    },
    {
      "id": 9,
      "date": "2017-06-20",
      "principal": 4554000,
      "interestFee": 571600
    },
    {
      "id": 10,
      "date": "2017-07-20",
      "principal": 4690600,
      "interestFee": 435000
    },
    {
      "id": 11,
      "date": "2017-08-21",
      "principal": 4831400,
      "interestFee": 294200
    },
    {
      "id": 12,
      "date": "2017-09-20",
      "principal": 4976600,
      "interestFee": 149300
    }
  ]
}
```

- Principal: ​total loan amount that a credit institute gives to the borrower and this is considered negative cash flow (outflow) while calculating IRR and APR
- UpfrontFee: a one time fee charged by the credit institute from the borrower and this is considered positive cash flow (inflow).
- Paybacks: ​a single payback is an amount that the borrower pays back the credit institute and this includes principal and interest. Number of paybacks are not fixed and depends on individual loan contract. Also, each payback amount could vary as well, unlike the example above where it is the same for all paybacks.

### Output

```json
{
  "apr": 48.3,
  "irr": 0.0334008783
}
```

- APR should be represented as per section 10.3.4 of https://www.handbook.fca.org.uk/handbook/MCOB/10.pdf
- IRR should be represented in decimals

Useful resources
----------------

To have a basic understanding to APR and IRR:
- https://en.wikipedia.org/wiki/Annual_percentage_rate
- https://en.wikipedia.org/wiki/Internal_rate_of_return

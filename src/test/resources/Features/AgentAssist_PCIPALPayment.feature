@AgentAssistPayment

Feature: POST : Agent assist payment

  Description: Do an end to end payment using the pcipal agent assist solution for all accounts.

  Scenario Outline: POST: Make a end to end agent assist pcipal payment for GB and NI

    When agent initiates payment with a reference number "<amount>", "<customerRefNum>", "<orderChannel>", "<sourceSystem>", "<account>", "<sourceObjectId>"
    Then verify that response status is 201
    And verify that payment is initiated and transaction reference is generated
    And then pcipal calls auth lambda and gets the token
    Then pcipal notifies and notification lambda puts the notification recieved in completed topic "<amount>", "<account>", "<merchantId>", "<status>", "<customerRefNum>", "<authorisedDate>", "<createdDate>","<processorId>", "<cardType>", "<lastFourDigits>", "<firstSixDigits>", "<authorizationId>", "<source>"
    Then verify that client self service SR has been created in siebel

    Examples:

      | amount | customerRefNum | orderChannel | sourceSystem | account | sourceObjectId | merchantId | status     | authorisedDate            | createdDate               | processorId  | cardType    | lastFourDigits | firstSixDigits | authorizationId | source |
      | 1000   | 121008429993   | moto         | siebel       | GB_MOTO | Sb100          | 23689674   | AUTHORISED | 2021-03-01T10:17:17.4080Z | 2021-03-01T10:17:17.4080Z | 3455355435   | Visa        | 1111           | 444433         | 34553           | siebel |
#      | 3000   | 121004084844   | moto         | siebel       | NI_MOTO | Sb102          | 23689674   | AUTHORISED | 2021-03-01T10:17:17.4080Z | 2021-03-01T10:17:17.4080Z | 183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | siebel |
#      | 2000   | 121004585316   | moto         | siebel       | GB_MOTO | Sb101          | 23689674   | AUTHORISED | 2021-03-01T10:17:17.4080Z | 2021-03-01T10:17:17.4080Z | 183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | siebel |


  Scenario Outline: POST: Agent makes a payment for an employer

    When agent initiates payment with a reference number "<amount>", "<customerRefNum>", "<orderChannel>", "<sourceSystem>", "<account>", "<sourceObjectId>"
    Then verify that response status is 201
    And verify that payment is initiated and transaction reference is generated
    And then pcipal calls auth lambda and gets the token
    Then pcipal notifies and notification lambda puts the notification recieved in completed topic "<amount>", "<account>", "<merchantId>", "<status>", "<customerRefNum>", "<authorisedDate>", "<createdDate>","<processorId>", "<cardType>", "<lastFourDigits>", "<firstSixDigits>", "<authorizationId>", "<source>"
    Then verify that employer self service SR has been created in siebel



    Examples:
      | amount | customerRefNum | orderChannel | sourceSystem | account  | sourceObjectId | merchantId | status     | authorisedDate            | createdDate               | processorId  | cardType    | lastFourDigits | firstSixDigits | authorizationId | source |
      | 4000   | 501000619237   | moto         | siebel       | ORG_MOTO | Sb103          | 23686584   | AUTHORISED | 2021-03-01T10:17:17.4080Z | 2021-03-01T10:17:17.4080Z | 183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | siebel |
#      | 6000   | 501002328913   | moto         | siebel       | ORG_MOTO | Sb104          | 23686584   | AUTHORISED | 2021-03-01T10:17:17.4080Z | 2021-03-01T10:17:17.4080Z | 183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | siebel |


@IVRpayment

Feature: POST : PCIPAL payment from IVR

  Description: IVR payment scenarios using different combinations

  Scenario Outline: POST: DO an end to end IVR payment for GB and NI
    When user initiates payment with his reference number "<customerRefNum>"
    Then verify that response status is 201
    And verify that ivr payment is initiated and transaction reference is generated "<customerRefNum>", "<account>", "<dateOfBirth>","<paymentAllowed>"
    And then pcipal calls auth lambda and gets the token
    Then pcipal notifies and notification lambda puts the payment status in completed topic"<amount>", "<account>", "<merchantId>", "<status>", "<customerRefNum>","<processorId>", "<cardType>", "<lastFourDigits>", "<firstSixDigits>", "<authorizationId>", "<source>"
    Then verify that client SR has been created in siebel
    Then verify that client has been notified for the payment done "<topic>"

    Examples:
| customerRefNum | account | dateOfBirth | paymentAllowed | amount | merchantId | status     |     processorId  | cardType    | lastFourDigits | firstSixDigits | authorizationId | source | topic                               |
| 121008429993   | GB_MOTO | 010187      | true           | 545    | 23689674   | AUTHORISED |     183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | IVR    | PersonsNotificationDelivered        |
#| 121004585316   | GB_MOTO | 010180      | true           | 2398   | 23689674   | AUTHORISED |     183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | IVR    | PersonsNotificationUnDelivered      |
#| 821000101997   | GB_MOTO | 010187      | true           | 1809   | 23689674   | AUTHORISED |     183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | IVR    | PersonsNotificationDelivered        |
#| 121004084844   | NI_MOTO | 010180      | true           | 9009   | 23686584   | AUTHORISED |     183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | IVR    | PersonsNotificationDeliveryFailed   |
##121008527329
##  121008075145
##  121008325387
##
  Scenario Outline: POST: DO an end to end IVR payment for Employer
    When user initiates payment with his reference number "<customerRefNum>"
    Then verify that response status is 201
    And verify that ivr payment is initiated and transaction reference is generated "<customerRefNum>", "<account>","<paymentAllowed>"
    And then pcipal calls auth lambda and gets the token
    Then pcipal notifies and notification lambda puts the payment status in completed topic"<amount>", "<account>", "<merchantId>", "<status>", "<customerRefNum>","<processorId>", "<cardType>", "<lastFourDigits>", "<firstSixDigits>", "<authorizationId>", "<source>"
#   Then verify that payment SR for employer has been created in siebel

    Examples:
 | customerRefNum | account  | paymentAllowed | amount | merchantId | status     |  processorId  | cardType    | lastFourDigits | firstSixDigits | authorizationId | source |
 | 501000619237   | ORG_MOTO | true           | 545    | 23686584   | AUTHORISED |  183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | IVR    |
# | 501002328913   | ORG_MOTO | true           | 445    | 23686584   | AUTHORISED |  183f2j8923j8 | Master-card | 1234           | 654321         | 55555           | IVR    |

  Scenario Outline: POST: Payment not allowed scenarios
    When user initiates payment with his reference number "<customerRefNum>"
    Then verify that response status is 200
    And verify that ivr payment is not initiated and payment is not allowed "<dateOfBirth>", "<paymentAllowed>"

    Examples:

 | customerRefNum | dateOfBirth | paymentAllowed |
 | 121008052728   | 070582      | false          |
 | 121008503161   | 010185      | false          |
 | 121008503189   | 010187      | false          |
 | 121008503170   | 010110      | false          |
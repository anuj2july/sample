@PortalOnlineGovPayCardFailure
Feature: POST: Testing for Card Payment Services Responses for all NEGATIVE SCENARIO for Portal

  Description: POST: The purpose of this feature is to test Card Payment Services Responses post browser launch for negative scenarios.

  @PortalOnlineCardDecline
  Scenario Outline: POST: Verify CPS for card decline status
    When user initiates payment with portal parameters "<amount>", "<customerRefNum>", "<additionalRefNum>", "<description>", "<return_url>", "<orderChannel>", "<sourceSystem>", "<account>"
    Then verify that response status is 201
    And verify the initial status for created payment
    And verify payment status after POST request
    Then launch the NextURL for filling card details
    And user enters card number "<CardNumber>", "<ExpiryMonth>", "<ExpiryYear>", "<NameOnCard>","<SecurityCode>",
    Then verify the confirmation page for decline payment

    Examples:
| amount | customerRefNum | additionalRefNum | description | return_url                                                                         | orderChannel | sourceSystem | account | CardNumber       | ExpiryMonth | ExpiryYear | NameOnCard | SecurityCode |
| 101    | 121004162072   | 121004674090     | Portal1       | https://www.test.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | NI      | 4000000000000002 | 12          | 2021       | Test User  | 879          |
| 101    | 121004162072   | 121004674090     | Online1       | https://www.test.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | NI      | 4000000000000002 | 12          | 2021       | Test User  | 879          |

  @PortalOnlineCardExpired
  Scenario Outline: POST: Verify CPS for expired card
    When user initiates payment with portal parameters "<amount>", "<customerRefNum>", "<additionalRefNum>", "<description>", "<return_url>", "<orderChannel>", "<sourceSystem>", "<account>"
    Then verify that response status is 201
    And verify the initial status for created payment
    And verify payment status after POST request
    Then launch the NextURL for filling card details
    And user enters card number "<CardNumber>", "<ExpiryMonth>", "<ExpiryYear>", "<NameOnCard>","<SecurityCode>",
    Then verify the confirmation page for decline payment

    Examples:
| amount | customerRefNum | additionalRefNum | description | return_url                                                                        | orderChannel | sourceSystem | account | CardNumber       | ExpiryMonth | ExpiryYear | NameOnCard | SecurityCode |
| 201    | 121004162072   | 121004674090     | portal2     | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | GB      | 4000000000000069 | 12          | 2021       | Test User  | 879          |
| 201    | 121004162072   | 121004674090     | online2     | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | GB      | 4000000000000069 | 12          | 2021       | Test User  | 879          |

  @PortalOnlineCardInvalidCVC
  Scenario Outline: POST: Verify CPS for Invalid CVC
    When user initiates payment with portal parameters "<amount>", "<customerRefNum>", "<additionalRefNum>", "<description>", "<return_url>", "<orderChannel>", "<sourceSystem>", "<account>"
    Then verify that response status is 201
    And verify the initial status for created payment
    And verify payment status after POST request
    Then launch the NextURL for filling card details
    And user enters card number "<CardNumber>", "<ExpiryMonth>", "<ExpiryYear>", "<NameOnCard>","<SecurityCode>",
    Then verify the confirmation page for decline payment

    Examples:
| amount | customerRefNum | additionalRefNum | description | return_url                                                                        | orderChannel | sourceSystem | account | CardNumber       | ExpiryMonth | ExpiryYear | NameOnCard | SecurityCode |
| 301    | 121004162072   | 121004674090     | portal3       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | GB      | 4000000000000127 | 12          | 2021       | Test User  | 879          |
| 301    | 121004162072   | 121004674090     | online3       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | GB      | 4000000000000127 | 12          | 2021       | Test User  | 879          |


  @PortalOnlineCardGeneralError
  Scenario Outline: POST: Verify CPS for General Error
    When user initiates payment with portal parameters "<amount>", "<customerRefNum>", "<additionalRefNum>", "<description>", "<return_url>", "<orderChannel>", "<sourceSystem>", "<account>"
    Then verify that response status is 201
    And verify the initial status for created payment
    And verify payment status after POST request
    Then launch the NextURL for filling card details
    And user enters card number "<CardNumber>", "<ExpiryMonth>", "<ExpiryYear>", "<NameOnCard>","<SecurityCode>",
    And user is presented technical problem page

    Examples:
| amount | customerRefNum | additionalRefNum | description | return_url                                                                        | orderChannel | sourceSystem | account | CardNumber       | ExpiryMonth | ExpiryYear | NameOnCard | SecurityCode |
| 401    | 121004162072   | 121004674090     | portal4       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | GB      | 4000000000000119 | 12          | 2021       | Test User  | 879          |
| 401    | 121004162072   | 121004674090     | online4       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | GB      | 4000000000000119 | 12          | 2021       | Test User  | 879          |



  @PortalOnlineMaestroCardNotAccepted
  Scenario Outline: POST: Verify CPS for Maestro card not accepted
    When user initiates payment with portal parameters "<amount>", "<customerRefNum>", "<additionalRefNum>", "<description>", "<return_url>", "<orderChannel>", "<sourceSystem>", "<account>"
    Then verify that response status is 201
    And verify the initial status for created payment
    And verify payment status after POST request
    Then launch the NextURL for filling card details
    And user enters card number "<CardNumber>", "<ExpiryMonth>", "<ExpiryYear>", "<NameOnCard>","<SecurityCode>",
    And user is presented with card not accepted page

    Examples:
| amount | customerRefNum | additionalRefNum | description | return_url                                                                        | orderChannel | sourceSystem | account | CardNumber       | ExpiryMonth | ExpiryYear | NameOnCard | SecurityCode |
| 501    | 121004162072   | 121004674090     | portal5       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | GB      | 6759649826438453 | 12          | 2021       | Test User  | 879          |
| 501    | 121004162072   | 121004674090     | online5       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | GB      | 6759649826438453 | 12          | 2021       | Test User  | 879          |
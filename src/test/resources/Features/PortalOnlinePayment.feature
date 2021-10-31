@PortalOnlineGovPay

Feature: POST: Portal and Online payment using gov pay
  Description: POST: Verify that end to end payment for cps works from portal and online.


  Scenario Outline: POST: DO a end to end successful payment from portal and online
    When user initiates payment with portal parameters "<amount>", "<customerRefNum>", "<additionalRefNum>", "<description>", "<return_url>", "<orderChannel>", "<sourceSystem>", "<account>"
    Then verify that response status is 201
    And verify the initial status for created payment
    Then launch the NextURL for filling card details
    And user enters card number "<CardNumber>", "<ExpiryMonth>", "<ExpiryYear>", "<NameOnCard>","<SecurityCode>",
    Then verify the confirmation page and click confirm payment
    And validate that you land on return url


    Examples:
 | amount | customerRefNum | additionalRefNum | description | return_url                                                                        | orderChannel | sourceSystem | account | CardNumber       | ExpiryMonth | ExpiryYear | NameOnCard   | SecurityCode |
| 161    | 121004200479   | 121004200479     | Test1       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | GB      | 4444333322221111 | 01          | 2023       | Test User 1  | 879          |
#| 162    | 121007986561   | 121007994199     | Test2       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | GB      | 4242424242424242 | 02          | 2024       | Test User 2  | 111          |
#| 163    | 121004162072   | 121007994199     | Test3       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | GB      | 4000056655665556 | 03          | 2025       | Test User 3  | 222          |
#| 164    | 121004162072   | 121007994199     | Test4       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | GB      | 4988080000000000 | 04          | 2024       | Test User 4  | 333          |
#| 165    | 121004162072   | 121007994199     | Test5       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | GB      | 4000160000000004 | 05          | 2025       | Test User 5  | 434          |
#| 156    | 121008034245   | 121007994199     | Test6       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | NI      | 4131840000000003 | 06          | 2026       | Test User 6  | 251          |
#| 157    | 121008034245   | 121007994199     | Test7       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | NI      | 4000620000000007 | 07          | 2027       | Test User 7  | 214          |
#| 158    | 121008034245   | 121007994199     | Test8       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | NI      | 4000000000000010 | 08          | 2028       | Test User 8  | 523          |
#| 159    | 121008034245   | 121007994199     | Test9       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | NI      | 5105105105105100 | 09          | 2029       | Test User 9  | 336          |
#| 160    | 121008034245   | 121007994199     | Test10      | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | NI      | 5200828282828210 | 10          | 2028       | Test User 10 | 888          |
#| 156    | 501000748414   | 501000748414     | Test11      | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | ORG     | 4131840000000003 | 11          | 2026       | Test User 11 | 251          |
#| 157    | 501000748414   | 501000748414     | Test12      | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | ORG     | 4000620000000007 | 12          | 2027       | Test User 12 | 214          |
#| 158    | 501000748414   | 501000748414     | Test13      | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | ORG     | 4000000000000010 | 01          | 2028       | Test User 13 | 523          |
#| 159    | 501000748414   | 501000748414     | Tes14       | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | online       | ORG     | 5105105105105100 | 02          | 2029       | Test User 14 | 336          |
#| 160    | 501000748414   | 501000748414     | Test15      | https://www.sit.awscmg-dev.dwpcloud.uk/payments/payment-ui-handler/v1/transStatus | internet     | portal       | ORG     | 5200828282828210 | 03          | 2029       | Test User 15 | 888          |
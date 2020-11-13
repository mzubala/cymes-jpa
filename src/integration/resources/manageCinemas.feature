Feature: cinemas network can be managed

  Scenario: the one where network admin creates a new cinema
    When network admin creates a cinema "Plaza" in "Lublin"
    Then admin cinemas list contains following cinemas:
      | city   | name  |
      | Lublin | Plaza |

  Scenario: the one where network admin creates multiple cinemas
    When network admin creates a cinema "Plaza" in "Lublin"
    And network admin creates a cinema "Złote Tarasy" in "Warszawa"
    And network admin creates a cinema "Arkadia" in "Warszawa"
    And network admin creates a cinema "Magnolia" in "Wrocław"
    Then admin cinemas list contains following cinemas:
      | city     | name         |
      | Lublin   | Plaza        |
      | Warszawa | Arkadia      |
      | Warszawa | Złote Tarasy |
      | Wrocław  | Magnolia     |

  Scenario: The one where network manager tries to create cinema that already exists
    When network admin creates a cinema "Plaza" in "Lublin"
    Then creating cinema "Plaza" in "Lublin" fails with conflict error

  Scenario: The one where network manager adds a cinema hall
    Given cinema "Plaza" in "Lublin" has been created
    When network admin creates cinema hall "9" in "Lublin" "Plaza":
      | _XXXXXX__XX_ |
      | _XXXXXX__XX_ |
      | _XXXXXX__XX_ |
      | XXXXXXXXXXXX |
    When network admin creates cinema hall "6" in "Lublin" "Plaza":
      | XXXXXX__ |
      | XXXXXX__ |
      | XXXXXX__ |
      | XXXXXXXX |
    Then cinema halls list in "Lublin" "Plaza" contains:
      | 6      |
      | 9      |
    And cinema hall "9" in "Lublin" "Plaza" contains following rows:
      | _XXXXXX__XX_ |
      | _XXXXXX__XX_ |
      | _XXXXXX__XX_ |
      | XXXXXXXXXXXX |
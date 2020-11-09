Feature: cinemas network can be managed

  Scenario: the one where network admin creates a new cinema
    When network admin creates a cinema "Plaza" in "Lublin"
    Then cinemas list contains following cinemas:
      | city   | name  |
      | Lublin | Plaza |

  Scenario: the one where network admin creates multiple cinemas
    When network admin creates a cinema "Plaza" in "Lublin"
    And network admin creates a cinema "Złote Tarasy" in "Warszawa"
    And network admin creates a cinema "Arkadia" in "Warszawa"
    And network admin creates a cinema "Magnolia" in "Wrocław"
    Then cinemas list contains following cinemas:
      | city     | name         |
      | Lublin   | Plaza        |
      | Warszawa | Arkadia      |
      | Warszawa | Złote Tarasy |
      | Wrocław  | Magnolia     |

  Scenario: The one where network manager tries to create cinema that already exists
    When network admin creates a cinema "Plaza" in "Lublin"
    Then creating cinema "Plaza" in "Lublin" fails with conflict error

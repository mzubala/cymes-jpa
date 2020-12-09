Feature: Buyer can see how much to pay for the tickets

  Background:
    Given network admin created movies:
      | title        | productionYear | description                                                                                 | duration | actors                                        | generes        |
      | Batman       | 1989           | Bezwzględny Joker zaczyna siać terror w Gotham.                                             | PT126M   | Michael Keaton, Jack Nicholson, Kim Basinger  | ACTION, SCI_FI |
      | Pulp Fiction | 1994           | Przemoc i odkupienie w opowieści o dwóch płatnych mordercach pracujących na zlecenie mafii. | PT154M   | John Travolta, Samuel L. Jackson, Uma Thurman | CRIME, DRAMA   |
    And cinema "Plaza" in "Lublin" has been created
    And cinema "Arkadia" in "Warszawa" has been created
    And network admin created cinema hall "1" in "Lublin" "Plaza":
      | XXX |
    And network admin created cinema hall "2" in "Lublin" "Plaza":
      | XXX |
    And network admin created cinema hall "1" in "Warszawa" "Arkadia":
      | XXX |
    And network admin created cinema hall "2" in "Warszawa" "Arkadia":
      | XXX |
    And network admin scheduled shows:
      | cinema  | city     | hall | movie        | startTime            |
      | Plaza   | Lublin   | 1    | Batman       | 2020-01-01T10:00:00Z |
      | Plaza   | Lublin   | 2    | Pulp Fiction | 2020-01-01T12:00:00Z |
      | Arkadia | Warszawa | 1    | Batman       | 2020-01-01T15:00:00Z |
      | Arkadia | Warszawa | 2    | Pulp Fiction | 2020-01-02T18:00:00Z |

  Scenario: The one where ticket prices for the movie apply
    When network admin defines show prices for movie "Pulp Fiction":
      | REGULAR | 40 |
      | STUDENT | 30 |
      | SENIOR  | 30 |
    And customer wants to buy tickets for "Pulp Fiction" in "Lublin" "Plaza" at "2020-01-01T12:00:00Z"
    Then customer sees following ticket prices:
      | REGULAR | 40 |
      | STUDENT | 30 |
      | SENIOR  | 30 |
    When customer selects to buy following tickets:
      | REGULAR | 2 |
      | SENIOR  | 1 |
    Then system calculates tickets price as "110":
      | ticketKind | count | price | total |
      | REGULAR    | 2     | 40    | 80    |
      | SENIOR     | 1     | 30    | 30    |

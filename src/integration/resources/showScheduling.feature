Feature: Shows can be scheduled

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

  Scenario: The one when network admin schedules shows and customers can see repertoire
    When network admin schedules shows:
      | cinema  | city     | hall | movie        | startTime            |
      | Plaza   | Lublin   | 1    | Batman       | 2020-01-01T10:00:00Z |
      | Plaza   | Lublin   | 2    | Pulp Fiction | 2020-01-01T12:00:00Z |
      | Arkadia | Warszawa | 1    | Batman       | 2020-01-01T15:00:00Z |
      | Arkadia | Warszawa | 2    | Pulp Fiction | 2020-01-02T18:00:00Z |
    Then customer can see repertoire for "Lublin" "Plaza" on "2020-01-01":
      | Batman       | 2020-01-01T10:00:00Z |
      | Pulp Fiction | 2020-01-01T12:00:00Z |
    And customer can see repertoire for "Warszawa" "Arkadia" on "2020-01-01":
      | Batman | 2020-01-01T15:00:00Z |
    And customer can see repertoire for "Warszawa" "Arkadia" on "2020-01-02":
      | Pulp Fiction | 2020-01-02T18:00:00Z |
Feature: movies catalog can be managed

  Scenario: The one when network admin creates a movie
    When network admin creates movies:
      | title  | productionYear | description                                     | duration | actors                                       | generes        |
      | Batman | 1989           | Bezwzględny Joker zaczyna siać terror w Gotham. | PT126M   | Michael Keaton, Jack Nicholson, Kim Basinger | ACTION, SCI_FI |
    And network admin creates movies:
      | title        | productionYear | description                                                                                 | duration | actors                                        | generes      |
      | Pulp Fiction | 1994           | Przemoc i odkupienie w opowieści o dwóch płatnych mordercach pracujących na zlecenie mafii. | PT154M   | John Travolta, Samuel L. Jackson, Uma Thurman | CRIME, DRAMA |
    Then admin movies list contains following movies:
      | title        |
      | Batman       |
      | Pulp Fiction |
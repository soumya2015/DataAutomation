Feature: Data Pipeline Simulation

  Scenario: Loading, cleaning, transforming, and saving data
    Given the unclean dataset "unclean_data.csv" is loaded
    When the data quality checks are applied to the dataset
    And  the data transformation and cleaning are applied to the loaded dataset
    Then a new file "final_data.csv" should be created successfully


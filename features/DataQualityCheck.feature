Feature: Data Quality Checks for SampleData.csv

  As a Quality Engineer,
  I want to ensure the dataset meets the required standards of completeness, consistency, uniqueness, and integrity
  So that the dataset can be used for accurate analysis.

  Background:
    Given the dataset is loaded from "SampleData.csv"

  Scenario: Validate Data Completeness
    When the dataset has the following required columns:
      | id     |
      | name   |
      | date   |
      | ammount|
    Then there are no missing values in the file for specified columns
    And Date column follows the format `YYYY-MM-DD`
    And there are no duplicate entries based on a unique identifier column
    And the results are logged in "data_quality_log.txt"


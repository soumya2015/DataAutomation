Feature: Clean and Transform Dataset from UncleanData.csv

  As a data engineer,
  I want to clean and transform the provided dataset
  So that the data is standardized and ready for analysis.

  Background:
    Given the dataset is loaded from "unclean_data.csv" for data transformation and cleaning

  Scenario: Apply Data Transformation and Cleaning to the loaded dataset

    When the dataset has the following required columns:
      | ID                |
      | Name              |
      | Transaction Date   |
      | Amount            |

    Then Check the dataset for duplicate values based on the 'ID' column
    And remove duplicate rows from the CSV file
    And standardize the 'Transaction Date' column to 'YYYY-MM-DD' format
    And trim whitespace and convert all text in the 'Name' column to lowercase
    And replace missing values in the Amount column with the median of the non-missing values
    And save the cleaned dataset to "cleaned_data.csv"



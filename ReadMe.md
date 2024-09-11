# Data Automation - BDD Cucumber Java Framework
## Overview
This project demonstrates the process of identifying data quality issues, applying data cleaning and transformation techniques, and simulating a data pipeline to ensure the dataset is ready for analysis. The goal is to improve the dataset's accuracy, consistency, and completeness through various tasks.

Additionally, this repository includes a BDD (Behavior-Driven Development) framework using Cucumber (v3.0.0) with Java. It showcases automation script development, utilizes multiple reporters like Allure, HTML, and JSON, and includes features like capturing screenshots for tests and generating error shots for failed test cases.

## Installation & Prerequisites
Before you begin, ensure you have the following tools installed and properly configured:

### JDK 17 
Ensure that the Java class path is correctly set.

### Maven
Ensure the .m2 class path is properly configured for Maven dependencies.

### IDE
IntelliJ IDEA or Eclipse can be used. If using Eclipse, the following plugins are required:

Maven
Cucumber
Framework Setup
To set up the framework, you can either:

Fork or clone this repository from GitHub, or
Download the ZIP file and extract it to your local workspace.
Follow these steps to set up the project in your preferred IDE and configure Maven to download the necessary dependencies.

## Running Sample Tests
To run the tests, follow these steps:

Open your terminal (e.g., iTerm for macOS or PowerShell for Windows).

Navigate to the project directory.

Execute the following command to run all the tests:

bash
Copy code
mvn clean test
To run a specific feature file, use the following command:

bash
Copy code
mvn test -Dcucumber.options="classpath:features/my_first.feature"
This will execute the my_first.feature file only.

Reporters
After executing the tests, the framework allows you to generate various types of reports to analyze test results:

## Cucumber Report
To generate a Cucumber report, you can use the following commands after test execution.

If you have configured an HTML Cucumber report in your pom.xml, the report can be opened by navigating to the following location:

## HTML & JUnit Reports: 

The framework automatically generates these reports, accessible in the target directory under cucumber-reports.
target/cucumber-reports/ directory.
## Contribution
We welcome and encourage contributions from the community to improve this project. Follow the steps below to contribute:

Fork the project repository to your own GitHub account.
Create a new branch for your changes.
Make the necessary changes, additions, or bug fixes in your branch.
Commit your changes with clear and concise messages explaining the purpose of each change.
If introducing new functionality, consider adding tests to ensure robustness.
Once your changes are ready, submit a pull request (PR) to the original repository.
In the PR description, provide a detailed explanation of the changes, including relevant context or background information.
The project maintainers will review your PR and provide feedback if needed.
Once approved, your changes will be merged into the main project.
Celebrate your successful contribution! 🎉
We value your time and effort in improving this project. Contributions are crucial to making it better, and we look forward to working with you!


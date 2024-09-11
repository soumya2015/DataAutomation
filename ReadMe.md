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
You can use either IntelliJ IDEA or Eclipse for this setup. The following plugins are required:

Maven
Cucumber
Framework Setup
To set up the framework, you have two options:

1.Fork or clone this repository from GitHub.

2.Download the ZIP file and extract it to your local workspace.

Follow these steps to set up the project in IntelliJ.
1.Open Intelij.

2.Go to File->Open

3.Select the File path for the "Data Automation" directory.

4.The Repository will be loaded in Intelij.

## Running Tests
To execute the tests, follow these steps:

1.Navigate to the CukesRunner file located at ..\src\main\java\com\lloyds\data\automation\runner.

2.Right-click on the CukesRunner file and select "Run" to start the tests.


## Cucumber Report
Once the test run completes successfully, the Cucumber report is generated in the ../target/HtmlReports directory. 
The report.html file can be opened using File Explorer or viewed in any web browser.
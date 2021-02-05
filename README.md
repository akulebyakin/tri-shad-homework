# TRI-SHAD Homework
This is the project for homework from Rita.

## Modules
### XML Compare (/xml-compare)
### Task 1:
1. Compare two XML files and test if them similar.
2. Use TestNG, Java 8, log4j
 
#### Logging
I use log4j2 with configuration file [log4j2.xml](https://github.com/akulebyakin/tri-shad-homework/blob/master/xml-compare/src/main/resources/log4j2.xml).
All logs goes to console and to the log file (_logs/output.log_ by default).

#### How to run tests
1. Set up properties

    ```bash
    task_one_gold_data=src/data/gold_data/A.xml
    task_one_output_data=src/data/output_data/B.xml
    task_one_ignore_nodes=paratext;cite.query
    ```
    
    where
    
    |Property|Description|
    |----------------|---------------------------------------------------------------------|
    |task_one_gold_data|Control XML file we will compare against|
    |task_one_output_data|XML file we will test and compare with Control|
    |task_one_ignore_nodes|XML node names (array of values separated with ';' or ',') to ignore during comparing. May be empty.|
     
    Another way to set up these properties is to pass it through CLI.
    
    _example:_
    ```bash
    mvn clean test -Ptask1 \
        -Dtask_one_gold_data=src/data/gold_data/A.xml \
        -Dtask_one_output_data=src/data/output_data/B.xml \
        -Dtask_one_ignore_nodes=paratext\;cite.query
    ```
    Note that I use this priority to get properties (from more important to less important)
    
    1. Environment variables
    2. System Properties
    3. Properties from test.properties file
    
    If both **test.properties** and **System Properties** have the same value, I use **System property** :)
2. Run tests using Maven

    ```bash
    mvn clean test -Ptask1
    ```

### Task 2:
1. Compare two big XML files and test if them similar.
2. Files are comparing by pairs with names A-B, A1-B1, A2-B2
3. All data files are in the Files.zip archive that we put to the project directory before test execution
4. Files from the archive extracts to appropriate directories (gold_data and output_data) automatically
5. Ignore tag <cite.query> while comparing. We only need to ignore the node definitions and save the text. 
The text should be compared as part of <paratext> node.
6. Add file error.log with 
 
#### Logging
All logs goes to console and to the log file (_logs/output.log_ by default). <br>
Comparison differences goes to file _logs/error.log_

#### How to run tests
1. Set up properties

    ```bash
    task_two_ignore_nodes_definitions=cite.query
    task_two_zip_data_file=Files.zip
    task_two_gold_data_folder=src/data/gold_data
    task_two_output_data_folder=src/data/output_data
    ```
    
    where
    
    |Property|Description|
    |----------------|---------------------------------------------------------------------|
    |task_two_ignore_nodes_definitions|Xml node names to ignore their definitions|
    |task_two_zip_data_file|Data files ZIP archive|
    |task_two_gold_data_folder|Folder with control XML files we will compare against|
    |task_two_output_data_folder|Folder with XML files we will test and compare with Control files|
     
    Another way to set up these properties is to pass it through CLI as mentioned above in the task1.
    
2. Put the ZIP archive with control and test files to the xlm-compare directory 
or wherever you want but set __task_two_zip_data_file__ with absolute file path then.
3. Run tests using Maven

    ```bash
    mvn clean test -Ptask2
    ```

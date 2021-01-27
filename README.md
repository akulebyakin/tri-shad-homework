# TRI-SHAD Homework
This is the project for homework from Rita.

## Modules
### XML Compare (/xml-compare)
Task:
1. Compare two XML files and test if them similar.
2. Use TestNG, Java 8, log4j
 
##### Logging
I use log4j2 with configuration file [log4j2.xml](https://github.com/akulebyakin/tri-shad-homework/blob/master/xml-compare/src/main/resources/log4j2.xml).
All logs goes to console and to the log file (_logs/output.log_ by default).

##### How to run tests
1. Set up properties

    ```bash
    gold_data=src/data/gold_data/A.xml
    output_data=src/data/output_data/B.xml
    ignore_nodes=paratext;cite.query
    ```
    
    where
    
    |Property|Description|
    |----------------|---------------------------------------------------------------------|
    |gold_data|Control XML file we will compare against|
    |output_data|XML file we will test and compare with Control|
    |ignore_nodes|XML node names (array of values separated with ';' or ',') to ignore during comparing. May be empty.|
     
    Another way to set up these properties is to pass it through CLI.
    
    _example:_
    ```bash
    mvn clean test \
        -Dgold_data=src/data/gold_data/A.xml \
        -Doutput_data=src/data/output_data/B.xml \
        -Dignore_nodes=paratext\;cite.query
    ```
    Note that I use this priority to get properties (from more important to less important)
    
    1. Environment variables
    2. System Properties
    3. Properties from test.properties file
    
    If both **test.properties** and **System Properties** have the same value, I use **System property** :)
2. Run tests using Maven

    ```bash
    mvn clean test
    ```

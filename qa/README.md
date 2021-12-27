Shelf presents simple storage cloud solution for users who will be able to upload and organize data structure on their shelves and execute lambda functions on the same cloud service.


This document presents a brief description of the testing process in our shelf app. Tech stack which will be used for this Shelf project to write and execute Automation tests is well known integrated development environment (IDE) framework software called IntelliJ IDEA.

- **OS: Win10**
- **Technology Used: Java (1.8), TestNG, Selenium, Maven**
- **Framework Used: Page Object Model (POM)**

Browser for test performing is previously defined in data property file and will use webdriver manager.
Other constants such as url pages, api keys etc are also defined in the same property file.


Functions organized in helpers folder which are divided into sub-packages will be used for writing clean and consistent code as per POM principles.


**How to run/execute Test:**

Button next to method signature JUnit @Test annotation will be used for running individual tests located in tests package folder, while all Regression, UI and API will be defined in suite folder.


![](C:\Users\Srdjan\Desktop\runtest.jpg)


**Dependencies used in this project:**


Dependency which allows triggering those functions for tests defining

- <dependency> 
    <groupId>org.testng</groupId> 
    <artifactId>testng</artifactId> 
    <version>6.8</version> 
    <scope>test</scope> 
</dependency> 

- <dependency> 
    <groupId>junit</groupId> 
    <artifactId>junit</artifactId> 
    <version>RELEASE</version> 
</dependency> 

Dependency used for MySQL database

<dependency> 
    <groupId>mysql</groupId> 
    <artifactId>mysql-connector-java</artifactId> 
    <version>8.0.27</version> 
</dependency> 

Dependency used for reading csv files

- <dependency> 
    <groupId>net.sf.opencsv</groupId> 
    <artifactId>opencsv</artifactId> 
    <version>2.3</version> 
</dependency> 


Dependency for driver management builder

- <dependency> 
    <groupId>io.github.bonigarcia</groupId> 
    <artifactId>webdrivermanager</artifactId> 
    <version>5.0.3</version> 
</dependency> 

Dependency used for JSON implementation

- <dependency> 
    <groupId>com.googlecode.json-simple</groupId> 
    <artifactId>json-simple</artifactId> 
    <version>1.1</version> 
    <scope>test</scope> 
</dependency> 

- <dependency> 
    <groupId>org.json</groupId> 
    <artifactId>json</artifactId> 
    <version>20171018</version> 
    <scope>test</scope> 
</dependency> 

Dependency used for easy performing testing of REST services

- <dependency> 
    <groupId>io.rest-assured</groupId> 
    <artifactId>rest-assured</artifactId> 
    <version>4.2.0</version> 
    <scope>test</scope> 
</dependency> 
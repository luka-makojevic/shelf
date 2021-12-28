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


**Dependencies used in this project:**


* selenium-java - [ UI Testing ]

  
* junit - [ UI & API Testing ]


* mysql-connector-java - [ DB Testing ]
 

* poi-ooxml - [ Reading excel files ]


* webdrivermanager - [ UI Testing - Driver builder ]


* json  - [ API Testing - JSON implementation ]


* rest-assured - [ API Testing ]
    
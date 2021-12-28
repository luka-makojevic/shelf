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


- <artifactId>selenium-java</artifactId> - [ UI Testing ]

  
- <artifactId>junit</artifactId> - [ UI & API Testing ]


- <artifactId>mysql-connector-java</artifactId> - [ DB Testing ]
 

- <artifactId>poi-ooxml</artifactId> - [ Reading excel files ]


- <artifactId>webdrivermanager</artifactId> - [ UI Testing - Driver builder ]


- <artifactId>json</artifactId>  - [ API Testing - JSON implementation ]


- <artifactId>rest-assured</artifactId> - [ API Testing ]
    
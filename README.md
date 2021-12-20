QMoney is a visual stock portfolio analyzer. It helps portfolio managers make trade recommendations for their clients.

Implemented the core logic of the portfolio manager and published it as a library.

Refactored code to add support for multiple stock quote services.

Improved application stability and performance.

QMoney Architecture 

![image](https://user-images.githubusercontent.com/24942039/145179072-53fa5359-a069-4546-8216-52e44f4c6912.png)





QMoney Portfolio Manager Interface


![image](https://user-images.githubusercontent.com/24942039/145179105-45a4c52e-c461-42d6-b89e-ee33e0205afb.png)



QMONEY MODULES

Fetch stock quotes and compute annualized stock returns

Scope of work:

1. Used Tiingo’s REST APIs to fetch stock quotes.
2. Computed the annualized returns based on stock purchase date and holding period.

Skills: Java, REST API, Jackson


Refactor using Java interfaces and publish a JAR file

Scope of work:

1. Refactored code to adapt to an updated interface contract published by the backend team.
2. Published the portfolio manager library as a JAR for easy versioning and distribution.
3. Created examples to help document library (JAR) usage.

Skills: Interfaces, Code Refactoring, Gradle


Improve application availability and stability

Scope of work:

1. Added support for a backup stock quote service (Alpha Vantage) to improve service availability.
2. Improved application stability with comprehensive error reporting and better exception handling.

Skills : Interfaces, Exception Handling






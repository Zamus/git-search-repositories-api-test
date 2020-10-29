# git-search-repositories-api-test
Repository for project on testing of Git's repository searches API

**Table of Contents**

- [Scenario](#scenario)
  * [Scope of this project](#scope-of-this-project)
- [Instructions on how to run](#instructions-on-how-to-run)


# Scenario
- We are Git testers and want to make sure quality of one of Git's API is up to the task. For this example, we are using https://api.github.com/search/repositories? .
- We are familiar with the documentation that's in https://developer.github.com/v3/search/#search-repositories.
- We picked at least 5 keywords and made test cases for querying, sorting and ordering of requests.
- We need to define and design a testing strategy, as well as implementing some automated test cases for it.
- CI implementation is out of scope for this, but it will get a mention in how it would work inside the testing strategy.
- Used a couple of libraries/frameworks that are commonly used along the testing community to accomplish this, so that we could focus on the strategy and scenarios.
  - Libraries used in this repository
    - JDK11
    - Maven 3.6.1
    - io.rest-assured v4.1.2
    - org.testng v7.0.0
    - io.rest-assured.json-schema-validator v4.1.2
    - com.fasterxml.jackson.core.jackson-databind v2.10.0
    - javax.xml.bind-jaxb-api v2.3.1
    - org.apache.maven.plugins.maven-surefire-plugin v2.18.1

## Scope of this project
- Testing process strategy: :white_check_mark:
- Automation of test cases on /search/repositories API for querying, sorting and ordering: :white_check_mark:
- Automation of sample JSON Schema Validation with manual recreation of a few properties the API has: :white_check_mark:
- Automation takes into consideration the API rate limiting: :white_check_mark:
- Instructions on how to run it: :white_check_mark:
- Custom user&repo data needed for some test cases: :white_check_mark:

- CI implementation: :x:
  - While this would make it a more complete solution, and it's considered in the testing strategy for scaling and making sure we can run our tests anytime/anywhere, because of time constraints it will not be done for this example.
  - At the same time, no test status visual reporting tools (e.g. Allure) are being used.
- Implementation of backend logic for API request-response handling: :x:
  - For this rest-assured will be used. As it's a very popular tool it provides use with ease of usage and community support. There are no use cases that demand a custom solution.
- Automation of performance testing: :x:
  - Although usually this would be included and it's in the strategy, it will not be implemented as would need a bit of time to become a real solution. Plus, Git already has rate-limiting in place.
- Automation of security testing: :x:
  - Implemented a couple tests using auth tokens for private repositories search, but not directly testing security.
  
  
# Instructions on how to run
This repository is made of two levels of testing: smoke and regression, with smoke being a very small subset of the regression suite.
This code was generated using:
- JDK11
- Maven 3.6.1

In order to run the tests at a **smoke** level, you need to navigate to the project root folder and execute this via the command line:

```mvn -Psmoke clean test```

In order to run the tests at a **regression** level, you need to navigate to the project root folder and execute this via the command line:

```mvn -Pregression clean test```

In order to run a specific test class, you need to navigate to the project root folder and execute this via the command line:

```mvn -Dtest=${testClassName} test```
e.g.
```mvn -Dtest=RateLimitTest test```

# git-search-repositories-api-test
Repository for project on testing of Git's repository searches API

**Table of Contents**

- [Scenario](#scenario)
  * [Scope of this project](#scope-of-this-project)
  * [Testing Strategy](#testing-strategy)
    + [Levels of testing](#levels-of-testing)
      - [Tests that are performed on a local environment and are not part of the implementation in this repository](#tests-that-are-performed-on-a-local-environment-and-are-not-part-of-the-implementation-in-this-repository)
      - [Tests that are executed against lower environments, like QA and STG](#tests-that-are-executed-against-lower-environments--like-qa-and-stg)
      - [Tests that are executed against production environments](#tests-that-are-executed-against-production-environments)
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
  - While this would make it a more complete solution, and it's considered in the testing strategy for scaling and making sure we can run our tests anytime/anywhere, but because of time constraints it will not be done for this example.
  - At the same time, no test status visual reporting tools (e.g. Allure) are being used.
- Implementation of backend logic for API request-response handling: :x:
  - For this example, the rest-assured library will be used. As it's a very popular tool it provides use with ease of usage and community support. There are no use cases that demand a custom solution.
- Automation of performance testing: :x:
  - Although usually this would be included and it's in the strategy, it will not be implemented as would need a bit of time to become a real solution. Plus, Git already has rate-limiting in place.
- Automation of security testing: :x:
  - Implemented a couple tests using auth tokens for private repositories search, but not directly testing security.
  
## Testing Strategy
**"Okay, soooo.... how would this fit into an API testing strategy for Git?"
Glad you asked!**

As stated before, I'm not exactly an SDET working @ Git. However, let's play the part.

The first question to answer is: what do we think the SDLC (software development life cycle) looks like. We don't really know, but for the sake of the exercise, we can jump into some assumptions:

- Git follows a BDD methodology. This works very well when your complete stack of stakeholders wants to use the products they make, and we are sure they do! 
	- This means that everyone is involved in creating software from the very begining. 
- Imaginate they don't already have a CI strategy (which I'm sure they do), they want to implement it, so that they can deliver very quickly to their community. Continuous integration means continuous feedback, which reflects in greater visibility, which also means less bugs!
- There are multiple stages to how they test their product. We will go into more detail in a bit. And these stages are executed and coordinatated between many people.
- SDETs and SWEs start writing tests as soon as they work out the specs with the PMs. These tests will lead their developments.
- Let's suppose Git releases a new major version of their APIs every quarter, with hotfixes and minors in between majors.

So a first look at our general testing strategy looks like this:

![](https://github.com/Zamus/git-search-repositories-api-test/blob/master/readme-images/Releases%20overview.png?raw=true)

At first, one may think that testing only happens in certain stages, but that's not true. SDET's tasks and responsibilities are all over the SDLC. To give you a more detailed view
![](https://github.com/Zamus/git-search-repositories-api-test/blob/master/readme-images/Releases%20detailed.png?raw=true)

Each activity has its own set of subtasks that we must do to prepare for that moment when we deploy (and even after that, as we will keep helping with the release after it's on production).

### Levels of testing

With that being said, based on the SDLC stages described, there are testing levels that are attached to each of these. To name a few where we can contribute and which can be very important to our CI strategy:

#### Tests that are performed on a local environment and are not part of the implementation in this repository
- Unit testing: these can be written by both the SWE and the SDET. These will be run against every PR that a person wants to merge to the "develop" (current release code) and "main" (production code) branches.
- Integration testing: same as before, but instead their testing influence is broader. We still test against mocked data and not real containers with our services.
- Contract testing: same as integration, will ensure services don't change their interfaces so that their interactions keep working as expected.
- Code correctness testing: these are small checks you can implement in your repository and that you can also execute on a PR-by-PR basis. For instance, if you were making JS code, you would want to have static code analysis tools like JSLint integrated with your builds in jenkins, and have a hook to Git (or whatever SCM tool you are using) that disables merges until these checks pass, which also applies to every test type mentioned above.

#### Tests that are executed against lower environments, like QA and STG
- Stress testing: will give us an idea on how the limits of load limits the services we want to deploy can handle. This also applies to other types of non-functional testing (compliance, security..)
- End-to-End testing: we have two different flavors for this: backend (can be against an API) and frontend/UI. Both will execute after initial checks are done on the code, and against a QA environment. We have two main levels and may have some in between:
	- Smoke: basic functionalities work, usually very few and quick to complete. Can be executed regularly, maybe not in a PR-by_PR basis but nightly on builds deployed on QA.
	- Regression: our full suite, with as many functional scenarios we can think off, and which may put the system under stress. Every test stage is important, but these will give us the most complete feedback of them all (usually they also take the longer to execute!) and will tell us if we can go to the next stage (QA -> STG -> PROD) or not.
	- [Optional] Sanity: some people use it as a middle level in the e2e, but for this example we are not considering it.

#### Tests that are executed against production environments
Yes, we want to keep testing on production. This part is not attached to any SDLC stage in particular, it actually happens from start to finish (and will continue across quarters).
We need to have live status on our services deployed on production. On one hand we will have metrics and alarms that will set off if something is wrong with our service.
On the other hand, we can reuse smoke tests and execute them on an hourly basis on production environments, the same way we do with "lower" environments but more frequently. Plus, they are lightweight and should be quick to complete.

  
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

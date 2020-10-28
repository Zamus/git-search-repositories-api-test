package com.ozamudio;

import org.testng.annotations.BeforeMethod;
import static io.restassured.RestAssured.given;

public class BaseTest {

    protected final String baseUrl = "https://api.github.com";
    protected final String statusUrl = baseUrl+"/status";
    protected final String searchUrl = baseUrl+"/search/repositories?q=";
    protected final String rateLimitUrl = baseUrl+"/rate_limit";
    protected final String authToken = "token eba39240d638775f27442504d5de73992a8a009b";

    @BeforeMethod
    public void beforeMethod() {
        waitIfRateLimited();
    }

    protected String getQueryParamBy(Qualifiers qualifier, Object argument) {
        return qualifier+":"+argument.toString();
    }

    /**
     * This method is important to make sure that any given test does not fail due to github's api ratelimiting
     * (10 req per minute if not authenticated)
     */
    private void waitIfRateLimited () {
        int response =
            given().
                when().
                    get(rateLimitUrl).
                then().
                    extract().jsonPath().getInt("resources.search.remaining");
        System.out.println("Rate limit remaining before start of the test: "+response);
        if (response < 1) {
            try {
                System.out.println("Pausing for 70 seconds for rate limit reset on API");
                Thread.sleep(70000);
            } catch (InterruptedException exception) {
                System.out.println("Unable to pause for rate limit reset on API. Test may fail because of this.");
                exception.printStackTrace();
            }
        }
    }
}

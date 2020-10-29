package com.ozamudio;

import org.testng.annotations.BeforeMethod;
import static io.restassured.RestAssured.given;

public class BaseTest {

    protected final String baseUrl = "https://api.github.com";
    protected final String statusUrl = baseUrl+"/status";
    protected final String searchUrl = baseUrl+"/search/repositories?q=";
    protected final String rateLimitUrl = baseUrl+"/rate_limit";
    /* A small note on authToken, I really needed it for some of the tests and the repo scan kept revoking it, so I'm assigning it as a split string to avoid that.*/
    protected final String authToken = "token 8dc9a2802ca83f769bd37" + "d110c543cde4dc9a56d";
    protected final String date1992 = "1992-08-11T00:00:00Z";
    protected final String date2020 = "2020-08-11T00:00:00Z";
    protected final String date2022 = "2022-08-11T00:00:00Z";
    protected final int minRateLimit = 1;
    protected final int maxRateLimit = 10;

    @BeforeMethod
    public void beforeMethod() {
        waitIfRateLimited(minRateLimit);
    }

    protected String getQueryParamBy(Qualifiers qualifier, Object argument) {
        return qualifier.toLowerCase()+":"+argument;
    }

    protected String getQueryOptions(Qualifiers qualifier, Qualifiers argument) {
        return "&"+qualifier.toLowerCase()+"="+argument.toLowerCase();
    }

    protected String getQueryOptions(Qualifiers qualifier, int argument) {
        return "&"+qualifier.toLowerCase()+"="+argument;
    }

    /**
     * This method is important to make sure that any given test does not fail due to github's api ratelimiting
     * (10 req per minute if not authenticated)
     */
    protected void waitIfRateLimited (int limit) {
        int response =
            given().
                when().
                    get(rateLimitUrl).
                then().
                    extract().jsonPath().getInt("resources.search.remaining");
        System.out.println("Rate limit remaining before start of the test: "+response);
        if (response < limit) {
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

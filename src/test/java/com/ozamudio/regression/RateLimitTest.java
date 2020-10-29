package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.RepositoryKeywords;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RateLimitTest extends BaseTest {

    /**
     * Testing if the rate limit of this API works. As an unauthenticated user, it's limit is 10 requests per minute,
     * so I left this as a variable in the BaseTest, in case a potential future developer would want to scale up the
     * tests with his authentication, or git itself changes it, modifying the imposed rate limit with it.
     */
    @Test
    public void testRateLimit() {
        waitIfRateLimited(maxRateLimit);
        for (int i = 0; i < maxRateLimit; i++) {
            given().
                when().
                    get(searchUrl+RepositoryKeywords.CATS).
                then().
                    assertThat().
                        statusCode(200);
        }
        given().
            when().
                get(searchUrl+RepositoryKeywords.CATS).
            then().
                assertThat().
                    statusCode(403);
    }

}

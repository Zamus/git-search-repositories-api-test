package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.Qualifiers;
import com.ozamudio.RepositoryKeywords;
import com.ozamudio.RepositoryNames;
import com.ozamudio.RepositoryOrganizations;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class RateLimitTest extends BaseTest {

    /**
     * Testing if the rate limit of this API works. As an unauthenticated user, it's limit is 10 requests per minute,
     * so I left this as a variable in the BaseTest, in case a potential future developer would want to scale up the
     * tests with his authentication, changing the imposed rate limit with it.
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

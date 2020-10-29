package com.ozamudio.smoke;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import com.ozamudio.BaseTest;
import com.ozamudio.Qualifiers;
import com.ozamudio.RepositoryFiles;
import com.ozamudio.RepositoryKeywords;
import com.ozamudio.RepositoryOrganizations;
import com.ozamudio.RepositoryOwners;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

/**
 * Smoke test cases, used for nightly or very frequent liveness check on the platform.
 * For this exercise, I included:
 *      - A ping to git /status endpoint. This executes at first so that we can have quick feedback on the API status
 *      and discard failure causes for upcoming tests.
 *      - The base path with one keyword returns a success and json type response.
 *      - Some simple tests where I use at least one of each type of restriction for the search query (keyword, owner and file).
 *
 */
public class SmokeTest extends BaseTest {

    @Test(priority = 1)
    public void testBasePathStatus() {
        given().
            when().
                get(statusUrl).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON);
    }

    @Test(priority = 2)
    public void testBasePathWithOneKeywordYieldsResults() {
        given().
            when().
                get(searchUrl+RepositoryKeywords.CATS).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("items.id.size()",greaterThanOrEqualTo(1));
    }

    @Test(priority = 2)
    public void testBasePathWithOneQualifierYieldsResults() {
        given().
            when().
                get(searchUrl+getQueryParamBy(Qualifiers.USER, RepositoryOwners.OZAMUDIOTESTOWNER)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("items.id.size()",greaterThanOrEqualTo(1));
    }

    @Test(priority = 2)
    public void testBasePathWithOneKeywordAndFileYieldsResults() {
        given().
            when().
                get(searchUrl+RepositoryKeywords.CATS+"+"+getQueryParamBy(Qualifiers.IN, RepositoryFiles.README)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("items.id.size()",greaterThanOrEqualTo(1));
    }

    @Test(priority = 2)
    public void testBasePathWithOnePublicOrgFileYieldsResults() {
        given().
            when().
                get(searchUrl+getQueryParamBy(Qualifiers.ORG, RepositoryOrganizations.WORKDAY)).
            then().
            assertThat().
                statusCode(200).
            and().
                contentType(ContentType.JSON).
            and().
                body("items.id.size()",greaterThanOrEqualTo(1));
    }

}
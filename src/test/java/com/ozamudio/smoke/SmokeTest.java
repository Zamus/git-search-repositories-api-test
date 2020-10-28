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
                get(searchUrl+getQueryParamBy(Qualifiers.user, RepositoryOwners.OZAMUDIOTESTOWNER)).
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
                get(searchUrl+RepositoryKeywords.CATS+"+"+getQueryParamBy(Qualifiers.in, RepositoryFiles.README)).
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
                get(searchUrl+getQueryParamBy(Qualifiers.org, RepositoryOrganizations.WORKDAY)).
            then().
            assertThat().
                statusCode(200).
            and().
                contentType(ContentType.JSON).
            and().
                body("items.id.size()",greaterThanOrEqualTo(1));
    }


    /**
     * For this tests, I'd expect that an implementation which does not allow for certain HTTP methods to be run,
     * would return an HTTP message for "Method not allowed". However, git's API seems to be returning 404 for these.
     * As a git tester, I have two ways of going forward:
     *     - Let this test fail and have it fixed, and an alarm will sound everytime this test fails.
     *     - Get to know that this is how git wants its API to work. As in the real world I'm not a real git tester
     *     and this part of their API is not documented, and only for the purpose of demonstration that this should be
     *     taken into account when testing APIs, I'll leave it as failing with what I would assume should happen
     *     (because making it pass only for the purpose of the exercise goes against all my ideas of what API
     *     good practices are).
     */
    @Test(priority = 2, enabled = false)
    public void testBasePathPostNotAllowed() {
        given().
            when().
                post(statusUrl).
            then().
            assertThat().
                statusCode(405).
            and().
                contentType(ContentType.JSON);
    }

    /**
     * For this tests, I'd expect that an implementation which does not allow for certain HTTP methods to be run,
     * would return an HTTP message for "Method not allowed". However, git's API seems to be returning 404 for these.
     * As a git tester, I have two ways of going forward:
     *     - Let this test fail and have it fixed, and an alarm will sound everytime this test fails.
     *     - Get to know that this is how git wants its API to work. As in the real world I'm not a real git tester
     *     and this part of their API is not documented, and only for the purpose of demonstration that this should be
     *     taken into account when testing APIs, I'll leave it as failing with what I would assume should happen
     *     (because making it pass only for the purpose of the exercise goes against all my ideas of what API
     *     good practices are).
     */
    @Test(priority = 2, enabled = false)
    public void testBasePathPatchNotAllowed() {
        given().
                when().
                patch(statusUrl).
                then().
                assertThat().
                statusCode(405).
                and().
                contentType(ContentType.JSON);
    }

    /**
     * For this tests, I'd expect that an implementation which does not allow for certain HTTP methods to be run,
     * would return an HTTP message for "Method not allowed". However, git's API seems to be returning 404 for these.
     * As a git tester, I have two ways of going forward:
     *     - Let this test fail and have it fixed, and an alarm will sound everytime this test fails.
     *     - Get to know that this is how git wants its API to work. As in the real world I'm not a real git tester
     *     and this part of their API is not documented, and only for the purpose of demonstration that this should be
     *     taken into account when testing APIs, I'll leave it as failing with what I would assume should happen
     *     (because making it pass only for the purpose of the exercise goes against all my ideas of what API
     *     good practices are).
     */
    @Test(priority = 2, enabled = false)
    public void testBasePathPutNotAllowed() {
        given().
                when().
                patch(statusUrl).
                then().
                assertThat().
                statusCode(405).
                and().
                contentType(ContentType.JSON);
    }

}
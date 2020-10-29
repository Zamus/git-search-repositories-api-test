package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.Qualifiers;
import com.ozamudio.RepositoryKeywords;
import com.ozamudio.RepositoryOrganizations;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class InvalidQueryTest extends BaseTest {

    @Test
    public void testBasePathWithoutKeywords() {
        given().
            when().
                get(searchUrl).
            then().
                assertThat().
                    statusCode(422).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("message", containsStringIgnoringCase("Validation Failed")).
                and().
                    body("errors.code[0]", containsStringIgnoringCase("missing"));
    }

    @Test
    public void testBasePathMalformed() {
        given().
            when().
                get(baseUrl+"/search/repositories?"+RepositoryKeywords.WORKDAY).
            then().
                assertThat().
                    statusCode(422).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("message", containsStringIgnoringCase("Validation Failed")).
                and().
                    body("errors.code[0]", containsStringIgnoringCase("missing"));
    }

    @Test
    public void testBasePathWithQueryTooLong() {
        String longQuery = IntStream.range(0, 257).mapToObj(i -> "a").collect(Collectors.joining(""));
        given().
            when().
                get(searchUrl+longQuery).
            then().
                assertThat().
                    statusCode(422).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("message", containsStringIgnoringCase("Validation Failed")).
                and().
                    body("errors.message[0]", containsStringIgnoringCase("The search is longer than 256 characters.")).
                and().
                    body("errors.code[0]", containsStringIgnoringCase("invalid"));
    }

    @Test
    public void testBasePathWithPrivateOrInexistentOrg() {
        given().
            when().
                get(searchUrl+getQueryParamBy(Qualifiers.ORG, RepositoryOrganizations.OZAMUDIOTESTORGANIZATION)).
            then().
                assertThat().
                    statusCode(422).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("message", containsStringIgnoringCase("Validation Failed")).
                and().
                    body("errors.message[0]", containsStringIgnoringCase("The listed users and repositories cannot be searched either because the resources do not exist or you do not have permission to view them.")).
                and().
                    body("errors.code[0]", containsStringIgnoringCase("invalid"));
    }

    @Test
    public void testBasePathWithInvalidDateFormat() {
        given().
            when().
                get(searchUrl + RepositoryKeywords.CATS + "+" + getQueryParamBy(Qualifiers.CREATED, ">YYYY-MM-DD")).
            then().
                assertThat().
                    statusCode(422).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("message", containsStringIgnoringCase("Validation Failed")).
                and().
                    body("errors.message[0]", containsStringIgnoringCase("YYYY-MM-DD\" is not a recognized date/time format. Please provide an ISO 8601 date/time value, such as YYYY-MM-DD.")).
                and().
                    body("errors.code[0]", containsStringIgnoringCase("invalid"));
    }

    @Test
    public void testBasePathWithInvalidDateRanges() {
        given().
            when().
                get(searchUrl + RepositoryKeywords.CATS + "+" + getQueryParamBy(Qualifiers.CREATED, date2022+".."+date1992)).
            then().
                assertThat().
                    statusCode(422).
                and().
                    body("message", containsStringIgnoringCase("Validation Failed")).
                and().
                    body("errors.message[0]", containsStringIgnoringCase("the lower bound of the range ("+date2022+" .. "+date1992+") is greater than the upper bound")).
                and().
                    body("errors.code[0]", containsStringIgnoringCase("invalid"));
    }

    /**
     * For this test, I'd expect that an implementation which does not allow for certain HTTP methods to be run,
     * would return an HTTP message for "Method not allowed". However, git's API seems to be returning 404 for these.
     * As a git tester, I have two ways of going forward:
     *     - Let this test fail and have it fixed, and an alarm will sound everytime this test fails.
     *     - Get to know that this is how git wants its API to work. As in the real world I'm not a real git tester
     *     and this part of their API is not documented, and only for the purpose of demonstration that this should be
     *     taken into account when testing APIs, I'll leave it as failing with what I would assume should happen
     *     (because making it pass only for the purpose of the exercise even if I impersonate a fake git tester
     *     goes against all my ideas of what API good practices are).
     */
    @Test
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
     * For this test, I'd expect that an implementation which does not allow for certain HTTP methods to be run,
     * would return an HTTP message for "Method not allowed". However, git's API seems to be returning 404 for these.
     * As a git tester, I have two ways of going forward:
     *     - Let this test fail and have it fixed, and an alarm will sound everytime this test fails.
     *     - Get to know that this is how git wants its API to work. As in the real world I'm not a real git tester
     *     and this part of their API is not documented, and only for the purpose of demonstration that this should be
     *     taken into account when testing APIs, I'll leave it as failing with what I would assume should happen
     *     (because making it pass only for the purpose of the exercise even if I impersonate a fake git tester
     *     goes against all my ideas of what API good practices are).
     */
    @Test
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
     * For this test, I'd expect that an implementation which does not allow for certain HTTP methods to be run,
     * would return an HTTP message for "Method not allowed". However, git's API seems to be returning 404 for these.
     * As a git tester, I have two ways of going forward:
     *     - Let this test fail and have it fixed, and an alarm will sound everytime this test fails.
     *     - Get to know that this is how git wants its API to work. As in the real world I'm not a real git tester
     *     and this part of their API is not documented, and only for the purpose of demonstration that this should be
     *     taken into account when testing APIs, I'll leave it as failing with what I would assume should happen
     *     (because making it pass only for the purpose of the exercise even if I impersonate a fake git tester
     *     goes against all my ideas of what API good practices are).
     */
    @Test
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


    @Test
    public void testBasePathWithInvalidQualifier() {
        given().
            when().
                get(searchUrl+getQueryParamBy(Qualifiers.INEXISTENT, "test")).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("items.id.size()", equalTo(0));
    }

}
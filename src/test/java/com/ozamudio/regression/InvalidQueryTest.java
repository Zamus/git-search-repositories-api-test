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
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

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
    public void testBasePathWithInvalidQualifier() {
        given().
            when().
                get(searchUrl+getQueryParamBy(Qualifiers.inexistent, "test")).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("items.id.size()", equalTo(0));
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
                get(searchUrl+getQueryParamBy(Qualifiers.org, RepositoryOrganizations.OZAMUDIOTESTORGANIZATION)).
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

}
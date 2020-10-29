package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.RepositoryKeywords;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ResponseSchemaTest extends BaseTest {

    /**
     * Very simple json schema validation test. It uses a representation of very few values I transcribed from Git's
     * /search/repositories API, as it was not available as such and had to recreate manually.
     */
    @Test
    public void testBasePathWithoutKeywordsSuccessSchemaMatches() {
        given().
            when().
                get(searchUrl+ RepositoryKeywords.CATS).
            then().
                assertThat().
                    statusCode(200).
                and().
                    body(JsonSchemaValidator.matchesJsonSchemaInClasspath("search-repo-schema.json"));
    }
}
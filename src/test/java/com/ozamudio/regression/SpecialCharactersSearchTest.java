package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.RepositoryKeywords;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class SpecialCharactersSearchTest extends BaseTest {

    @Test
    public void testBasePathWithOneSpecialCharacterKeywordYieldsResults() {
        given().
            when().
                get(searchUrl+RepositoryKeywords.ж).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("items.id.size()",greaterThanOrEqualTo(1));
    }

    @Test
    public void testBasePathWithCombinationOfSpecialCharactersKeywordIsAccepted() {
        given().
            when().
                get(searchUrl+RepositoryKeywords.火+RepositoryKeywords.CATS).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("items.id.size()",greaterThanOrEqualTo(1));
    }
}
package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.Qualifiers;
import com.ozamudio.RepositoryLanguages;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class FilteredSearchTest extends BaseTest {

    @Test
    public void testBasePathWithOneLanguageYieldsResults() {
        given().
            when().
                get(searchUrl + getQueryParamBy(Qualifiers.language, RepositoryLanguages.ASSEMBLY)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", greaterThanOrEqualTo(1)).
                and().
                    body("items.size()", greaterThanOrEqualTo(1)).
                and().
                    body("items.language", everyItem(equalToIgnoringCase(RepositoryLanguages.ASSEMBLY.toString())));
    }

}

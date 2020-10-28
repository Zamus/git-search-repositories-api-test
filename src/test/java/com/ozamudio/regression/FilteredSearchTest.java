package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.Qualifiers;
import com.ozamudio.RepositoryKeywords;
import com.ozamudio.RepositoryLanguages;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

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

    @Test
    public void testBasePathWithTwoLanguagesYieldsResults() {
        System.out.println(searchUrl + getQueryParamBy(Qualifiers.language, RepositoryLanguages.JAVA) + "+" + getQueryParamBy(Qualifiers.language, RepositoryLanguages.JAVASCRIPT));
        given().
            when().
                get(searchUrl + getQueryParamBy(Qualifiers.language, RepositoryLanguages.JAVA.toString()) + "+" + getQueryParamBy(Qualifiers.language, RepositoryLanguages.JAVASCRIPT.toString())).
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
                    body("items.language", everyItem(in(Arrays.asList(RepositoryLanguages.JAVA.toString(), RepositoryLanguages.JAVASCRIPT.toString()))));
    }

    @Test
    public void testBasePathWithOneLanguageAndSinceCreatedDateYieldsResults() {
        given().
            when().
                get(searchUrl + getQueryParamBy(Qualifiers.language, RepositoryLanguages.ASSEMBLY) + "+" + getQueryParamBy(Qualifiers.created, ">"+date1992)).
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
                    body("items.language", everyItem(equalToIgnoringCase(RepositoryLanguages.ASSEMBLY.toString()))).
                and().
                    body("items.created_at", everyItem(greaterThan(date1992)));
    }

    @Test
    public void testBasePathWithCreatedSinceDateYieldsResults() {
        given().
            when().
                get(searchUrl + RepositoryKeywords.CATS + "+" + getQueryParamBy(Qualifiers.created, ">"+date1992)).
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
                    body("items.created_at", everyItem(greaterThan(date1992)));
    }

    @Test
    public void testBasePathWithCreatedSinceOrEqualDateYieldsResults() {
        given().
            when().
                get(searchUrl + RepositoryKeywords.CATS + "+" + getQueryParamBy(Qualifiers.created, ">="+date1992)).
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
                    body("items.created_at", everyItem(greaterThanOrEqualTo(date1992)));
    }

    @Test
    public void testBasePathWithCreatedBeforeOrEqualDateYieldsResults() {
        given().
            when().
                get(searchUrl + RepositoryKeywords.CATS + "+" + getQueryParamBy(Qualifiers.created, "<="+date2020)).
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
                    body("items.created_at", everyItem(lessThanOrEqualTo(date2020)));
    }

    @Test
    public void testBasePathWithCreatedBeforeDateYieldsResults() {
        given().
                when().
                get(searchUrl + RepositoryKeywords.CATS + "+" + getQueryParamBy(Qualifiers.created, "<"+date2020)).
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
                body("items.created_at", everyItem(lessThan(date2020)));
    }

    @Test
    public void testBasePathWithFutureSinceDateYieldsNoResults() {
        given().
            when().
                get(searchUrl + RepositoryKeywords.CATS + "+" + getQueryParamBy(Qualifiers.created, ">"+date2022)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", equalTo(0)).
                and().
                    body("items.size()", equalTo(0));
    }


    @Test
    public void testBasePathWithDatesUnionYieldsResults() {
        given().
            when().
                get(searchUrl + RepositoryKeywords.CATS + "+" + getQueryParamBy(Qualifiers.created, date1992+".."+date2020)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    body("total_count", greaterThanOrEqualTo(1)).
                and().
                    body("items.size()", greaterThanOrEqualTo(1)).
                and().
                    body("items.created_at", everyItem(greaterThan(date1992))).
                and().
                    body("items.created_at", everyItem(lessThan(date2020)));

    }

}

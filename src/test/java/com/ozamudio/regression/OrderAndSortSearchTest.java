package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.Qualifiers;
import com.ozamudio.RepositoryKeywords;
import com.ozamudio.RepositoryLanguages;
import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class OrderAndSortSearchTest extends BaseTest {

    @Test
    public void testBasePathOrderingByStarsDesc() {
        List<Integer> starsValues = given().
            when().
                get(searchUrl + RepositoryKeywords.CODE + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.DESC)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", greaterThanOrEqualTo(1)).
                and().
                    body("items.size()", greaterThanOrEqualTo(1)).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), greaterThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsDescWithMaxResults() {
        List<Integer> starsValues = given().
            when().
                get(searchUrl + RepositoryKeywords.CODE + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.DESC) + getQueryOptions(Qualifiers.PER_PAGE, 100)).
            then().
                assertThat().
                   statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", greaterThanOrEqualTo(1)).
                and().
                    body("items.size()", equalTo(100)).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), greaterThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsDescInACertainStarsRange() {
        List<Integer> starsValues = given().
                when().
                get(searchUrl + RepositoryKeywords.CODE + "+" + getQueryParamBy(Qualifiers.STARS, "<100") + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.ASC) + getQueryOptions(Qualifiers.PER_PAGE, 100)).
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                and().
                body("total_count", greaterThanOrEqualTo(1)).
                and().
                body("items.size()", equalTo(100)).
                and().
                body("items.stargazers_count", everyItem(lessThanOrEqualTo(100))).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), greaterThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsDescInACertainLanguage() {
        List<Integer> starsValues = given().
            when().
                get(searchUrl + RepositoryKeywords.CODE + "+" + getQueryParamBy(Qualifiers.LANGUAGE, RepositoryLanguages.ASSEMBLY) + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.DESC)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", greaterThanOrEqualTo(1)).
                and().
                    body("items.language", everyItem(equalToIgnoringCase(RepositoryLanguages.ASSEMBLY.toString()))).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), greaterThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsDescInACertainForksRange() {
        List<Integer> starsValues = given().
                when().
                    get(searchUrl + RepositoryKeywords.CODE + "+" + getQueryParamBy(Qualifiers.FORKS, "1..5") + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.DESC)).
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
                        body("items.forks", everyItem(lessThanOrEqualTo(5))).
                    and().
                        body("items.forks", everyItem(greaterThanOrEqualTo(1))).
                    extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), greaterThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsDescWithLowMaxResults() {
        List<Integer> starsValues = given().
            when().
                get(searchUrl + RepositoryKeywords.CODE + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.DESC) + getQueryOptions(Qualifiers.PER_PAGE, 5)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", greaterThanOrEqualTo(1)).
                and().
                    body("items.size()", equalTo(5)).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), greaterThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsAsc() {
        List<Integer> starsValues = given().
                when().
                get(searchUrl + RepositoryKeywords.CODE + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.ASC)).
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                and().
                body("total_count", greaterThanOrEqualTo(1)).
                and().
                body("items.size()", greaterThanOrEqualTo(1)).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), greaterThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsAscWithMaxResults() {
        List<Integer> starsValues = given().
                when().
                get(searchUrl + RepositoryKeywords.CODE + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.ASC) + getQueryOptions(Qualifiers.PER_PAGE, 100)).
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                and().
                body("total_count", greaterThanOrEqualTo(1)).
                and().
                body("items.size()", equalTo(100)).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), lessThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsAscInACertainStarsRange() {
        List<Integer> starsValues = given().
            when().
                get(searchUrl + RepositoryKeywords.CODE + "+" + getQueryParamBy(Qualifiers.STARS, "<100") + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.ASC) + getQueryOptions(Qualifiers.PER_PAGE, 100)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", greaterThanOrEqualTo(1)).
                and().
                    body("items.size()", equalTo(100)).
                and().
                    body("items.stargazers_count", everyItem(lessThanOrEqualTo(100))).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), lessThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsAscInACertainLanguage() {
        List<Integer> starsValues = given().
            when().
                get(searchUrl + RepositoryKeywords.CODE + "+" + getQueryParamBy(Qualifiers.LANGUAGE, RepositoryLanguages.ASSEMBLY) + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.ASC)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", greaterThanOrEqualTo(1)).
                and().
                    body("items.language", everyItem(equalToIgnoringCase(RepositoryLanguages.ASSEMBLY.toString()))).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), lessThanOrEqualTo(starsValues.get(i+1)));
        }
    }

    @Test
    public void testBasePathOrderingByStarsAscInACertainForksRange() {
        List<Integer> starsValues = given().
            when().
                get(searchUrl + getQueryParamBy(Qualifiers.FORKS, "1..10") + getQueryOptions(Qualifiers.SORT, Qualifiers.STARS) + getQueryOptions(Qualifiers.ORDER, Qualifiers.ASC)).
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
                    body("items.forks", everyItem(lessThanOrEqualTo(10))).
                and().
                    body("items.forks", everyItem(greaterThanOrEqualTo(1))).
                extract().jsonPath().getList("items.stargazers_count");
        for (int i = 0; i < starsValues.size()-1; i++) {
            MatcherAssert.assertThat(starsValues.get(i), lessThanOrEqualTo(starsValues.get(i+1)));
        }
    }

}

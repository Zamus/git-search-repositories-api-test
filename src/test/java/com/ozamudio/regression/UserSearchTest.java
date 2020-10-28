package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.Qualifiers;
import com.ozamudio.RepositoryKeywords;
import com.ozamudio.RepositoryNames;
import com.ozamudio.RepositoryOwners;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class UserSearchTest extends BaseTest {

    @Test
    public void testBasePathWithUserPublicRepos() {
        given().
            when().
                get(searchUrl + getQueryParamBy(Qualifiers.user, RepositoryOwners.OZAMUDIOTESTOWNER)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", equalTo(2)).
                and().
                    body("items.size()", equalTo(2)).
                and().
                    body("items.name[0]", equalToIgnoringCase(RepositoryNames.TESTREPO2.toString())).
                and().
                    body("items.name[1]", equalToIgnoringCase(RepositoryNames.TESTREPO1.toString()));
    }

    @Test
    public void testBasePathWithSpecificUserPublicRepo() {
        given().
            when().
                get(searchUrl + RepositoryNames.TESTREPO1 + "+" + getQueryParamBy(Qualifiers.user, RepositoryOwners.OZAMUDIOTESTOWNER)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", equalTo(1)).
                and().
                    body("items.size()", equalTo(1)).
                and().
                    body("items.name[0]", equalToIgnoringCase(RepositoryNames.TESTREPO1.toString()));
    }

    @Test
    public void testBasePathWithUserPrivateRepos() {
        given().
            header("Authorization", authToken).
            when().
                get(searchUrl+getQueryParamBy(Qualifiers.user, RepositoryOwners.OZAMUDIOTESTOWNER)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", equalTo(3)).
                and().
                    body("items.size()", equalTo(3)).
                and().
                    body("items.name.last()", equalToIgnoringCase(RepositoryNames.PRIVATEREPO.toString()));
    }

    @Test
    public void testBasePathWithUserInexistentRepo() {
        given().
            when().
                get(searchUrl + RepositoryKeywords.WORKDAY + "+" + getQueryParamBy(Qualifiers.user, RepositoryOwners.OZAMUDIOTESTOWNER)).
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
}

package com.ozamudio.regression;

import com.ozamudio.BaseTest;
import com.ozamudio.Qualifiers;
import com.ozamudio.RepositoryKeywords;
import com.ozamudio.RepositoryNames;
import com.ozamudio.RepositoryOrganizations;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class OrgSearchTest extends BaseTest {

    @Test
    public void testBasePathWithOrgPublicRepos() {
        given().
            when().
                get(searchUrl + getQueryParamBy(Qualifiers.org, RepositoryOrganizations.WORKDAY)).
            then().
                assertThat().
                    statusCode(200).
                and().
                    contentType(ContentType.JSON).
                and().
                    body("total_count", greaterThanOrEqualTo(1)).
                and().
                    body("items.size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void testBasePathWithPrivateOrg() {
        given().
            header("Authorization", authToken).
            when().
                get(searchUrl + getQueryParamBy(Qualifiers.org, RepositoryOrganizations.OZAMUDIOTESTORGANIZATION)).
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
                    body("items.name[0]", equalToIgnoringCase(RepositoryNames.PRIVATEORGREPO.toString()));
    }

    @Test
    public void testBasePathWithOrgInexistentRepo() {
        given().
            header("Authorization", authToken).
            when().
                get(searchUrl + RepositoryKeywords.WORKDAY + "+" + getQueryParamBy(Qualifiers.org, RepositoryOrganizations.OZAMUDIOTESTORGANIZATION)).
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

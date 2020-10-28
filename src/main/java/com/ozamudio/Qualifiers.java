package com.ozamudio;

public enum Qualifiers {
    IN,
    USER,
    ORG,
    SIZE,
    FOLLOWERS,
    STARS,
    FORKS,
    CREATED,
    LANGUAGE,
    INEXISTENT,
    SORT,
    ORDER,
    PER_PAGE,
    DESC,
    ASC;

    public String toLowerCase() {
        return name().toLowerCase();
    }
}
package com.ozamudio;

public enum Qualifiers {
    IN,
    USER,
    ORG,
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
package ksbysample.webapp.bootnpmgeb.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("MissingOverride")
@Getter
@AllArgsConstructor
public enum SexValues implements Values {

    MALE("1", "男性")
    , FEMALE("2", "女性");

    private final String value;
    private final String text;

}

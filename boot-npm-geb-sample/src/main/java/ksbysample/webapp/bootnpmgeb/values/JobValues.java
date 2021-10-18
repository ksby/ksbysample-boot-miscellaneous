package ksbysample.webapp.bootnpmgeb.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ???
 */
@SuppressWarnings("MissingOverride")
@Getter
@AllArgsConstructor
public enum JobValues implements Values {

    EMPLOYEE("1", "会社員"), STUDENT("2", "学生"), OTHER("3", "その他");

    private final String value;
    private final String text;

}

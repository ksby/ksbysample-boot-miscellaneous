package ksbysample.webapp.bootnpmgeb.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("MissingOverride")
@Getter
@AllArgsConstructor
public enum Type1Values implements Values {

    PRODUCT("1", "製品に関するお問い合わせ")
    , RECRUIT("2", "人事・採用情報")
    , OTHER("3", "その他");

    private final String value;
    private final String text;

}

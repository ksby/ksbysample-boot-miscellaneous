package ksbysample.webapp.bootnpmgeb.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("MissingOverride")
@Getter
@AllArgsConstructor
public enum Type2Values implements Values {

    ESTIMATE("1", "見積が欲しい")
    , CATALOGUE("2", "資料が欲しい")
    , OTHER("3", "その他の問い合わせ");

    private final String value;
    private final String text;

}

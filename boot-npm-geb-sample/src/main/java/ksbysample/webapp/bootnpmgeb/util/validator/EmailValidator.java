package ksbysample.webapp.bootnpmgeb.util.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * メールアドレスチェック用 Util クラス
 */
public class EmailValidator {

    private static final int MAX_ELEMENTS_LENGTH = 2;
    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[\\x21-\\x7E]+$");

    /**
     * メールアドレスが正しいフォーマットかチェックする
     *
     * @param email チェックするメールアドレス
     * @return チェック結果 true:OK, false:NG
     */
    public static boolean validate(String email) {
        // 値が入力されていなければチェックしない
        if (StringUtils.isEmpty(email)) {
            return true;
        }

        // @で分割して要素数が２つかどうかチェックする
        String[] elements = email.split("@");
        if (elements.length != MAX_ELEMENTS_LENGTH) {
            return false;
        }

        // １つ目及び２つ目の要素に空白、制御文字、非ASCII文字が含まれていないかチェックする
        return Arrays.stream(elements)
                .map(e -> PATTERN_EMAIL.matcher(e).matches())
                .reduce(true, (prs, prv) -> prs && prv);
    }

}

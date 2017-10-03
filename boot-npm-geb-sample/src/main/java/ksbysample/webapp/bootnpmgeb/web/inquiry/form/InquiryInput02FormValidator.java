package ksbysample.webapp.bootnpmgeb.web.inquiry.form;

import ksbysample.webapp.bootnpmgeb.util.validator.EmailValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

/**
 * 入力画面２入力チェック用クラス
 */
@Component
public class InquiryInput02FormValidator implements Validator {

    private static final Pattern PATTERN_ZIPCODE = Pattern.compile("^[0-9]{7}$");
    private static final Pattern PATTERN_TEL1 = Pattern.compile("^0.*");
    private static final Pattern PATTERN_TEL1_TEL2 = Pattern.compile("^[0-9]{6}$");
    private static final Pattern PATTERN_TEL3 = Pattern.compile("^[0-9]{4}$");

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(InquiryInput02Form.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InquiryInput02Form inquiryInput02Form = (InquiryInput02Form) target;

        checkZipcode(inquiryInput02Form.getZipcode1(), inquiryInput02Form.getZipcode2(), errors);

        checkTelAndEmail(inquiryInput02Form.getTel1()
                , inquiryInput02Form.getTel2()
                , inquiryInput02Form.getTel3()
                , inquiryInput02Form.getEmail()
                , errors);
    }

    private void checkZipcode(String zipcode1, String zipcode2, Errors errors) {
        if (StringUtils.isEmpty(zipcode1) && StringUtils.isEmpty(zipcode2)) {
            return;
        }

        if (!PATTERN_ZIPCODE.matcher(zipcode1 + zipcode2)
                .matches()) {
            errors.reject("InquiryInput02Form.zipcode.UnmatchPattern");
        }
    }

    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.ConfusingTernary"})
    private void checkTelAndEmail(String tel1, String tel2, String tel3, String email, Errors errors) {
        if (StringUtils.isEmpty(tel1)
                && StringUtils.isEmpty(tel2)
                && StringUtils.isEmpty(tel3)
                && StringUtils.isEmpty(email)) {
            return;
        }

        if (StringUtils.isEmpty(tel1 + tel2 + tel3)
                && StringUtils.isEmpty(email)) {
            errors.reject("InquiryInput02Form.telOrEmail.NotEmpty");
        } else {
            // 電話番号に１つでも入力されていたら入力チェックする
            if (StringUtils.isNoneEmpty(tel1)
                    || StringUtils.isNotEmpty(tel2)
                    || StringUtils.isNotEmpty(tel3)) {
                if (StringUtils.isEmpty(tel1)
                        || StringUtils.isEmpty(tel2)
                        || StringUtils.isEmpty(tel3)) {
                    errors.reject("InquiryInput02Form.tel1AndTel2AndTel3.AnyEmpty");
                } else if (!PATTERN_TEL1.matcher(tel1).matches()) {
                    errors.reject("InquiryInput02Form.tel1.UnmatchPattern");
                } else if (!PATTERN_TEL1_TEL2.matcher(tel1 + tel2).matches()) {
                    errors.reject("InquiryInput02Form.tel1AndTel2.UnmatchPattern");
                } else if (!PATTERN_TEL3.matcher(tel3).matches()) {
                    errors.reject("InquiryInput02Form.tel3.UnmatchPattern");
                }
            }

            // メールアドレスが入力されていたら入力チェックする
            if (StringUtils.isNotEmpty(email)) {
                if (!EmailValidator.validate(email)) {
                    errors.reject("InquiryInput02Form.email.Invalid");
                }
            }
        }
    }
}

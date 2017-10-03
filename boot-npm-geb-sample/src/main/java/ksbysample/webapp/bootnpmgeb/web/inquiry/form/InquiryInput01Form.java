package ksbysample.webapp.bootnpmgeb.web.inquiry.form;

import ksbysample.webapp.bootnpmgeb.values.JobValues;
import ksbysample.webapp.bootnpmgeb.values.SexValues;
import ksbysample.webapp.bootnpmgeb.values.validation.ValuesEnum;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 入力画面１用 Form クラス
 */
@Data
public class InquiryInput01Form implements Serializable {

    private static final long serialVersionUID = -7017712118767300185L;

    @NotEmpty
    @Size(min = 1, max = 20)
    private String lastname;

    @NotEmpty
    @Size(min = 1, max = 20)
    private String firstname;

    @NotEmpty
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[\\u3041-\\u3096]+$")
    private String lastkana;

    @NotEmpty
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[\\u3041-\\u3096]+$")
    private String firstkana;

    @ValuesEnum(enumClass = SexValues.class)
    private String sex;

    @NotEmpty
    @Digits(integer = 3, fraction = 0)
    private String age;

    @ValuesEnum(enumClass = JobValues.class, allowEmpty = true)
    private String job;

    private boolean copiedFromSession = false;

}

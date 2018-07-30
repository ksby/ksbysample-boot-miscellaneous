package ksbysample.webapp.bootnpmgeb.web.inquiry.form;


import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 入力画面２　必須チェック用クラス
 */
@Data
public class InquiryInput02FormNotEmptyRule {

    private static final long serialVersionUID = -2484970766971811218L;

    @NotEmpty
    private String zipcode1;

    @NotEmpty
    private String zipcode2;

    @NotEmpty
    private String address;

    private String tel1;

    private String tel2;

    private String tel3;

    private String email;

    private boolean copiedFromSession;

}

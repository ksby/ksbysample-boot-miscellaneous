package ksbysample.webapp.bootnpmgeb.web.inquiry.form;


import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 入力画面２用 Form クラス
 */
@Data
public class InquiryInput02Form implements Serializable {

    private static final long serialVersionUID = -2484970766971811218L;

    @Size(max = 3)
    private String zipcode1;

    @Size(max = 4)
    private String zipcode2;

    @Size(max = 256)
    private String address;

    @Size(max = 5)
    private String tel1;

    @Size(max = 4)
    private String tel2;

    @Size(max = 4)
    private String tel3;

    @Size(max = 256)
    private String email;

    private boolean copiedFromSession = false;

    private boolean ignoreCheckRequired = false;

}

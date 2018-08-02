package ksbysample.webapp.bootnpmgeb.web.inquiry.form;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 入力画面３　必須チェック用クラス
 */
@Data
public class InquiryInput03FormNotEmptyRule {

    @NotEmpty
    private String type1;

    @NotEmpty
    private List<String> type2 = new ArrayList<>();

    @NotEmpty
    private String inquiry;

    private List<String> survey;

    private boolean copiedFromSession;

}

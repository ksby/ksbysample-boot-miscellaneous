package ksbysample.webapp.bootnpmgeb.web.inquiry.form;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 入力画面３用 Form クラス
 */
@Data
public class InquiryInput03Form implements Serializable {

    private static final long serialVersionUID = -2818250124844174764L;

    private String type1;

    private List<String> type2 = new ArrayList<>();

    private String inquiry;

    private List<String> survey = new ArrayList<>();

    private boolean copiedFromSession;

}

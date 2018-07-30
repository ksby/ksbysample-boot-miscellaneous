package ksbysample.webapp.bootnpmgeb.web.inquiry.form;

import lombok.Data;

import java.util.List;

/**
 * 確認画面用 Form クラス
 */
@Data
public class ConfirmForm {

    /************************
     * 入力画面１の入力項目用
     ************************/
    private String name;

    private String kana;

    private String sex;

    private String age;

    private String job;

    /************************
     * 入力画面２の入力項目用
     ************************/
    private String zipcode;

    private String address;

    private String tel;

    private String email;

    /************************
     * 入力画面３の入力項目用
     ************************/
    private String type1;

    private String type2;

    private String inquiry;

    private List<String> survey;

}

package geb.page.inquiry

import geb.Page
import geb.module.FormModule

class InquiryInput01Page extends Page {

    static url = "/inquiry/input/01"
    static at = { title == "入力フォーム - 入力画面１" }
    static content = {
        form { module FormModule }
    }

    static initialValueList = [
            "#lastname"        : "",
            "#firstname"       : "",
            "#lastkana"        : "",
            "#firstkana"       : "",
            "input[name='sex']": null,
            "#age"             : "",
            "#job"             : ""
    ]

    static maxLengthValueList = [
            "#lastname" : "あ" * 20,
            "#firstname": "あ" * 20,
            "#lastkana" : "あ" * 20,
            "#firstkana": "あ" * 20,
            "#age"      : "9" * 3,
    ]

}

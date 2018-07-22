package geb.page.inquiry

import geb.Page
import geb.module.FormModule
import ksbysample.webapp.bootnpmgeb.values.JobValues
import ksbysample.webapp.bootnpmgeb.values.SexValues

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
            "#job"             : "",
    ]

    static maxLengthValueList = [
            "#lastname"        : "田" * 20,
            "#firstname"       : "太" * 20,
            "#lastkana"        : "あ" * 20,
            "#firstkana"       : "い" * 20,
            "input[name='sex']": SexValues.MALE.value,
            "#age"             : "9" * 3,
            "#job"             : JobValues.EMPLOYEE.value,
    ]

    static valueList01 = [
            "#lastname"        : "田中",
            "#firstname"       : "太郎",
            "#lastkana"        : "たなか",
            "#firstkana"       : "たろう",
            "input[name='sex']": SexValues.MALE.value,
            "#age"             : "30",
            "#job"             : JobValues.EMPLOYEE.value,
    ]

}

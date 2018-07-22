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
            "#lastname" : "a" * 20,
            "#firstname": "a" * 20,
            "#lastkana" : "a" * 20,
            "#firstkana": "a" * 20,
            "#age"      : "9" * 3,
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

package geb.page.inquiry

import geb.Page
import geb.module.FormModule

class InquiryInput02Page extends Page {

    static url = "/inquiry/input/01"
    static at = { title == "入力フォーム - 入力画面２" }
    static content = {
        form { module FormModule }
    }

    static initialValueList = [
            "#zipcode1": "",
            "#zipcode2": "",
            "#address" : "",
            "#tel1"    : "",
            "#tel2"    : "",
            "#tel3"    : "",
            "#email"   : "",
    ]

    static maxLengthValueList = [
            "#zipcode1": "x" * 3,
            "#zipcode2": "x" * 4,
            "#address" : "あ" * 256,
            "#tel1"    : "9" * 5,
            "#tel2"    : "9" * 4,
            "#tel3"    : "9" * 4,
            "#email"   : "x" * 256,
    ]

}

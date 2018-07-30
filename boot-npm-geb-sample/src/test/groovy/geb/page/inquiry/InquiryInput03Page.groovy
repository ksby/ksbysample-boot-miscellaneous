package geb.page.inquiry

import geb.Page
import geb.module.FormModule
import ksbysample.webapp.bootnpmgeb.values.Type1Values
import ksbysample.webapp.bootnpmgeb.values.Type2Values

class InquiryInput03Page extends Page {

    static url = "/inquiry/input/03"
    static at = { title == "入力フォーム - 入力画面３" }
    static content = {
        form { module FormModule }
    }

    static initialValueList = [
            "#type1"              : "",
            "input[name='type2']" : null,
            "#inquiry"            : "",
            "input[name='survey']": null,
    ]

    static valueList01 = [
            "#type1"              : Type1Values.PRODUCT.value,
            "input[name='type2']" : [
                    Type2Values.CATALOGUE.value,
                    Type2Values.ESTIMATE.value,
                    Type2Values.OTHER.value,
            ],
            "#inquiry"            : "これは\nテスト\nです",
            "input[name='survey']": ["1", "2", "3", "4", "5", "6", "7", "8"],
    ]

}

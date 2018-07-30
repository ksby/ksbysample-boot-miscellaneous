package geb.page.inquiry

import geb.Page
import geb.module.FormModule
import ksbysample.webapp.bootnpmgeb.values.JobValues
import ksbysample.webapp.bootnpmgeb.values.SexValues
import ksbysample.webapp.bootnpmgeb.values.Type1Values
import ksbysample.webapp.bootnpmgeb.values.Type2Values

import java.util.stream.Collectors

class InquiryConfirmPage extends Page {

    static url = "/inquiry/confirm"
    static at = { title == "入力フォーム - 確認画面" }
    static content = {
        form { module FormModule }
    }

    static textList01 = [
            "#name"   : "田中　太郎",
            "#kana"   : "たなか　たろう",
            "#sex"    : SexValues.MALE.text,
            "#age"    : "30 歳",
            "#job"    : JobValues.EMPLOYEE.text,
            "#zipcode": "〒 100-0005",
            "#address": "東京都千代田区飯田橋１－１",
            "#tel"    : "03-1234-5678",
            "#email"  : "taro.tanaka@sample.co.jp",
            "#type1"  : Type1Values.PRODUCT.text,
            "#type2"  : [Type2Values.ESTIMATE.text
                         , Type2Values.CATALOGUE.text
                         , Type2Values.OTHER.text].stream()
                    .collect(Collectors.joining("、")),
            "#inquiry": "これは\nテスト\nです",
    ]

}

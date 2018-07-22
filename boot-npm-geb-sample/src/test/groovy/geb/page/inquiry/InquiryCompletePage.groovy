package geb.page.inquiry

import geb.Page
import geb.module.FormModule
import org.openqa.selenium.By

class InquiryCompletePage extends Page {

    static url = "/inquiry/confirm"
    static at = { title == "入力フォーム - 完了画面" }
    static content = {
        form { module FormModule }
        btnToInput01 { $(By.xpath("//button[contains(text(), '入力画面へ')]")) }
    }

}

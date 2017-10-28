package geb.page.inquiry

import geb.Page

class InquiryInput01Page extends Page {

    static url = "/inquiry/input/01"
    static at = { title == "入力フォーム - 入力画面１" }
    static content = {
        btnNext { $(".js-btn-next") }
    }

}

package geb.module

import geb.Module
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement

/**
 * フォーム共通の content や、テストに使用するメソッドを定義するクラス
 */
class FormModule extends Module {

    static content = {
        btnBack { $(".js-btn-back") }
        btnNext { $(".js-btn-next") }
    }

    /**
     * Form の入力項目に値を一括セットする
     * valueList は以下の形式の Map である
     * <pre>{@code
     * static initialValueList = [
     *      "#lastname"        : "",
     *      "#firstname"       : "",
     *      "#lastkana"        : "",
     *      "#firstkana"       : "",
     *      "input[name='sex']": null,
     *      "#age"             : "",
     *      "#job"             : ""
     * ]
     *}</pre>
     *
     * @param valueList セットするセレクタと値を記述した Map
     */
    void setValueList(valueList) {
        valueList.each {
            $(it.key).value(it.value)
            $(it.key) << Keys.TAB
        }
    }

    /**
     * セレクタに値がセットされているかを検証する
     * このメソッドは Spock の then, expect で使用する想定である
     *
     * @param valueList 検証するセレクタと値を記述した Map
     * @return true 固定
     */
    boolean assertValueList(valueList) {
        valueList.each { key, value ->
            if ($(key).first().attr("type") == "radio") {
                WebElement element = $(key).allElements().find { it.selected }
                if (element == null) {
                    assert null == value
                } else {
                    assert element.getAttribute("value") == value
                }
            } else {
                assert $(key).value() == value
            }
        }
        true
    }

}

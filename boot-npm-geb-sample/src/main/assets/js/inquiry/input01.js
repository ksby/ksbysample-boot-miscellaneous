"use strict";

var $ = require("admin-lte/plugins/jQuery/jquery-2.2.3.min.js");
var Form = require("lib/class/Form.js");
var converter = require("lib/util/converter.js");
var validator = require("lib/util/validator.js");

var form = new Form([
    "#lastname",
    "#firstname",
    "#lastkana",
    "#firstkana",
    "input:radio[name='sex']",
    "#age",
    "#job"
]);

var nameValidator = function (event) {
    var idFormGroup = "#form-group-name";
    var idList = ["#lastname", "#firstname"];
    form.convertAndValidate(form, event, idFormGroup, idList,
        undefined,
        function () {
            validator.checkRequired(form, idFormGroup, idList, "お名前（漢字）を入力してください");
        }
    );
};

var kanaValidator = function (event) {
    var idFormGroup = "#form-group-kana";
    var idList = ["#lastkana", "#firstkana"];
    form.convertAndValidate(form, event, idFormGroup, idList,
        function () {
            converter.convertHiragana(idList);
        },
        function () {
            validator.checkRequired(form, idFormGroup, idList, "お名前（かな）を入力してください");
            validator.checkHiragana(form, idFormGroup, idList, "お名前（かな）にはひらがなを入力してください");
        }
    );
};

var sexValidator = function (event) {
    var idFormGroup = "#form-group-sex";
    var idList = ["input:radio[name='sex']"];
    form.convertAndValidate(form, event, idFormGroup, idList,
        undefined,
        function () {
            validator.checkRequired(form, idFormGroup, idList, "性別を選択してください");
        }
    );
};

var ageValidator = function (event) {
    var idFormGroup = "#form-group-age";
    var idList = ["#age"];
    form.convertAndValidate(form, event, idFormGroup, idList,
        function () {
            converter.convertHanAlphaNumeric(idList);
        },
        function () {
            validator.checkRequired(form, idFormGroup, idList, "年齢を入力してください");
            validator.checkRegexp(form, idFormGroup, idList, "^[0-9]+$", "年齢には数字を入力してください");
        }
    );
};

var jobValidator = function (event) {
    var idFormGroup = "#form-group-job";
    var idList = ["#job"];
    form.setSuccess(idFormGroup);
};

var btnNextClickHandler = function (event) {
    // 全ての入力チェックを実行する
    form.forceAllFocused(form);
    [
        nameValidator,
        kanaValidator,
        sexValidator,
        ageValidator,
        jobValidator
    ].forEach(function (validator) {
        validator(event);
    });
    // 入力チェックエラーがある場合には処理を中断する
    if (event.isPropagationStopped()) {
        // 一番最初のエラーの項目にカーソルを移動する
        $(".has-error:first :input:first").focus().select();
        return false;
    }

    // 「次へ」ボタンをクリック不可にする
    $(".js-btn-next").prop("disabled", true);

    // サーバにリクエストを送信する
    $("#input01Form").attr("action", "/inquiry/input/01/?move=next");
    $("#input01Form").submit();

    // return false は
    // event.preventDefault() + event.stopPropagation() らしい
    return false;
};

$(document).ready(function () {
    // 入力チェック用の validator 関数をセットする
    $("#lastname").on("blur", nameValidator);
    $("#firstname").on("blur", nameValidator);
    $("#lastkana").on("blur", kanaValidator);
    $("#firstkana").on("blur", kanaValidator);
    $("input:radio[name='sex']").on("blur", sexValidator);
    $("#age").on("blur", ageValidator);
    $("#job").on("blur", jobValidator);

    // 「次へ」ボタンクリック時の処理をセットする
    $(".js-btn-next").on("click", btnNextClickHandler)
});

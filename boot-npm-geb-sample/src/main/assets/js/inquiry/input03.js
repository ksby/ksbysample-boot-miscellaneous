"use strict";

var Form = require("lib/class/Form.js");
var validator = require("lib/util/validator.js");

var form = new Form([
    "#type1",
    "input:checkbox[name='type2']",
    "#inquiry",
    "input:checkbox[name='survey']"
]);

var type1Validator = function (event) {
    var idFormGroup = "#form-group-type1";
    var idList = ["#type1"];
    form.convertAndValidate(form, event, idFormGroup, idList,
        undefined,
        function () {
            validator.checkRequired(form, idFormGroup, idList, "お問い合わせの種類１を選択してください");
        }
    );
};

var type2Validator = function (event) {
    var idFormGroup = "#form-group-type2";
    var idList = ["input:checkbox[name='type2']"];
    form.convertAndValidate(form, event, idFormGroup, idList,
        undefined,
        function () {
            validator.checkRequired(form, idFormGroup, idList, "お問い合わせの種類２を選択してください");
        }
    );
};

var inquiryValidator = function (event) {
    var idFormGroup = "#form-group-inquiry";
    var idList = ["#inquiry"];
    form.convertAndValidate(form, event, idFormGroup, idList,
        undefined,
        function () {
            validator.checkRequired(form, idFormGroup, idList, "お問い合わせの内容を入力してください");
        }
    );
};

var executeAllValidator = function (event) {
    form.forceAllFocused(form);
    [
        type1Validator,
        type2Validator,
        inquiryValidator
    ].forEach(function (validateFunction) {
        validateFunction(event);
    });
};

var btnBackOrNextClickHandler = function (event, url, ignoreCheckRequired) {
    // 全ての入力チェックを実行する
    try {
        if (ignoreCheckRequired) {
            validator.ignoreCheckRequired = ignoreCheckRequired;
            form.backupFocusedState(form);
        }
        executeAllValidator(event);
    } finally {
        if (ignoreCheckRequired) {
            validator.reset();
            form.restoreFocusedState(form);
        }
    }
    // 入力チェックエラーがある場合には処理を中断する
    if (event.isPropagationStopped()) {
        // 一番最初のエラーの項目にカーソルを移動する
        $(".has-error:first :input:first").focus().select();
        return false;
    }

    // 「前の画面へ戻る」「次へ」ボタンをクリック不可にする
    $(".js-btn-back").prop("disabled", true);
    $(".js-btn-confirm").prop("disabled", true);

    // サーバにリクエストを送信する
    // --- $("#ignoreCheckRequired").val(ignoreCheckRequired);
    $("#inquiryInput03Form").attr("action", url);
    $("#inquiryInput03Form").submit();

    // return false は
    // event.preventDefault() + event.stopPropagation() らしい
    return false;
};

$(document).ready(function (event) {
    // 入力チェック用の validator 関数をセットする
    $("#type1").on("blur", type1Validator);
    $("input:checkbox[name='type2']").on("blur", type2Validator);
    $("#inquiry").on("blur", inquiryValidator);

    // 「前の画面へ戻る」「次へ」ボタンクリック時の処理をセットする
    $(".js-btn-back").on("click", function (e) {
        return btnBackOrNextClickHandler(e, "/inquiry/input/03/?move=back", true);
    });
    $(".js-btn-confirm").on("click", function (e) {
        return btnBackOrNextClickHandler(e, "/inquiry/input/03/?move=next", false);
    });

    // 初期画面表示時にセッションに保存されていたデータを表示する場合には
    // 入力チェックを実行して画面の表示を入力チェック後の状態にする
    if ($("#copiedFromSession").val() === "true") {
        executeAllValidator(event);
    }

    // 「お問い合わせの種類１」にフォーカスをセットする
    $("#type1").focus().select();
});

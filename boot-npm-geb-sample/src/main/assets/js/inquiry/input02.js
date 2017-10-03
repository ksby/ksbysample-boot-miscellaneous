"use strict";

var Form = require("lib/class/Form.js");
var converter = require("lib/util/converter.js");
var validator = require("lib/util/validator.js");

var form = new Form([
    "#zipcode1",
    "#zipcode2",
    "#address",
    "#tel1",
    "#tel2",
    "#tel3",
    "#email"
]);

var zipcodeValidator = function (event) {
    var idFormGroup = "#form-group-zipcode";
    var idList = ["#zipcode1", "#zipcode2"];
    form.convertAndValidate(form, event, idFormGroup, idList,
        function () {
            converter.convertHanAlphaNumeric(idList);
        },
        function () {
            validator.checkRequired(form, idFormGroup, idList, "郵便番号を入力してください");
            validator.checkRegexp(form, idFormGroup, idList, "^[0-9]{7}$", "郵便番号は７入力してください");
        }
    );
};

var addressValidator = function (event) {
    var idFormGroup = "#form-group-address";
    var idList = ["#address"];
    form.convertAndValidate(form, event, idFormGroup, idList,
        undefined,
        function () {
            validator.checkRequired(form, idFormGroup, idList, "住所を入力してください");
        }
    );
};

var telAndEmailValidator = function (event) {
    var telIdFormGroup = "#form-group-tel";
    var emailIdFormGroup = "#form-group-email";
    var telIdList = ["#tel1", "#tel2", "#tel3"];
    var emailIdList = ["#email"];
    var idList = telIdList.concat(emailIdList);

    var validateFunction = function () {
        if (validator.ignoreCheckRequired && form.isAllEmpty(idList)) {
            return;
        }

        if (form.isAllEmpty(idList)) {
            var errmsg = "電話番号とメールアドレスのいずれか一方を入力してください";
            form.setError(telIdFormGroup, errmsg);
            form.setError(emailIdFormGroup, errmsg);
            throw new Error(errmsg);
        } else {
            // 最初に入力チェックOKの状態にしておく
            form.setSuccess(telIdFormGroup);
            form.setSuccess(emailIdFormGroup);
            var errmsg = "";

            // 「電話番号」に１つでも値が入力されていたら入力チェックする
            if (form.isAnyNotEmpty(telIdList)) {
                try {
                    validator.forceCheckRequired(form, telIdFormGroup, telIdList,
                        "市外局番、市内局番、加入者番号は全て入力してください");
                    validator.checkRegexp(form, telIdFormGroup, ["#tel1"],
                        "^0", "市外局番の先頭には 0 の数字を入力してください");
                    validator.checkRegexp(form, telIdFormGroup, ["#tel1", "#tel2"],
                        "^[0-9]{6}$", "市外局番＋市内局番の組み合わせが数字６桁になるように入力してください");
                    validator.checkRegexp(form, telIdFormGroup, ["#tel3"],
                        "^[0-9]{4}$", "加入者番号には４桁の数字を入力してください");
                } catch (e) {
                    errmsg = e.message;
                }
            }

            // 「メールアドレス」が入力されていたら入力チェックする
            if (form.isAnyNotEmpty(emailIdList)) {
                try {
                    emailIdList.forEach(function (id) {
                        validator.checkEmail(form, emailIdFormGroup, id, "メールアドレスを入力してください");
                    })
                } catch (e) {
                    errmsg = e.message;
                }
            }

            // 入力チェックエラーが発生している場合には Error オブジェクトを throw する
            if (errmsg !== "") {
                throw new Error(errmsg);
            }
        }
    };

    form.convertAndValidate(form, event, telIdFormGroup, idList,
        function () {
            converter.convertHanAlphaNumeric(telIdList);
        },
        validateFunction
    );
    form.convertAndValidate(form, event, emailIdFormGroup, idList,
        function () {
            converter.convertHanAlphaNumeric(emailIdList);
        },
        validateFunction
    );
};

var executeAllValidator = function (event) {
    form.forceAllFocused(form);
    [
        zipcodeValidator,
        addressValidator,
        telAndEmailValidator
    ].forEach(function (validator) {
        validator(event);
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
    $(".js-btn-next").prop("disabled", true);

    // サーバにリクエストを送信する
    $("#inquiryInput02Form").attr("action", url);
    $("#inquiryInput02Form").submit();

    // return false は
    // event.preventDefault() + event.stopPropagation() らしい
    return false;
};

$(document).ready(function () {
    // 入力チェック用の validator 関数をセットする
    $("#zipcode1").on("blur", zipcodeValidator);
    $("#zipcode2").on("blur", zipcodeValidator);
    $("#address").on("blur", addressValidator);
    $("#tel1").on("blur", telAndEmailValidator);
    $("#tel2").on("blur", telAndEmailValidator);
    $("#tel3").on("blur", telAndEmailValidator);
    $("#email").on("blur", telAndEmailValidator);

    // 「前の画面へ戻る」「次へ」ボタンクリック時の処理をセットする
    $(".js-btn-back").on("click", function (event) {
        return btnBackOrNextClickHandler(event, "/inquiry/input/02/?move=back", true);
    });
    $(".js-btn-next").on("click", function (event) {
        return btnBackOrNextClickHandler(event, "/inquiry/input/02/?move=next", false);
    });

    // 初期画面表示時にセッションに保存されていたデータを表示する場合には
    // 入力チェックを実行して画面の表示を入力チェック後の状態にする
    if ($("#copiedFromSession").val() === "true") {
        executeAllValidator(event);
    }

    // 「郵便番号」の左側の項目にフォーカスをセットする
    $("#zipcode1").focus().select();
});

"use strict";

var $ = require("admin-lte/plugins/jQuery/jquery-2.2.3.min.js");

module.exports = Form;

function Form(idList) {
    this.idList = idList;
    this.focused = [];
    addFocusEventListener(this);
}

/**
 * 変換＆入力チェックを行う
 * @param {Form} form - Form オブジェクト
 * @param {jQuery.Event} event - イベント発生時に渡された jQuery.Event オブジェクト
 * @param {string} idFormGroup - Validation の SUCCESS/ERROR の結果を反映する要素の id
 * @param {Array} idList - チェックを行う要素の id の配列
 * @param {Function} converter - 変換処理を行う関数、変換しない場合には undefined を渡す
 * @param {Function} validator - 入力チェックを行う関数
 */
Form.prototype.convertAndValidate = function (form, event, idFormGroup, idList, converter, validator) {
    if (form.isAllFocused(form, idList)) {
        if (converter !== undefined) {
            converter();
        }

        if (validator !== undefined) {
            form.resetValidation(idFormGroup);
            try {
                validator();
            } catch (e) {
                event.stopPropagation();
            }
        }
    }
};

/**
 * idList で渡された id の要素全てに focused イベントが発生済かをチェックする
 * @param {Form} form - Form オブジェクト
 * @param {Array} idList - チェックする id がセットされた配列
 * @returns {boolean} true:発生した, false:発生していない
 */
Form.prototype.isAllFocused = function (form, idList) {
    return idList.reduce(function (p, id) {
        return (form.focused[id] === undefined) ? false : p;
    }, true);
};

/**
 * idList で渡された id の要素の中に未入力／未選択のものがあるかチェックする
 * @param {Array} idList - チェックする id がセットされた配列
 * @returns {boolean} true:未入力／未選択のものがある, false:全て入力／選択されている
 */
Form.prototype.isAnyEmpty = function (idList) {
    var anyEmpty = false;
    idList.forEach(function (id) {
        if ($(id).attr("type") === "radio") {
            if ($(id + ":checked").val() === undefined) {
                anyEmpty = true;
            }
        } else if ($(id).attr("type") === "select") {

        } else if ($(id).val() === "") {
            anyEmpty = true;
        }
    });
    return anyEmpty;
};

/**
 * 指定された id の要素から has-success, has-error クラスを取り除き、エラーメッセージを非表示にする
 * @param {string} idFormGroup - Validation の状態をリセットする id
 */
Form.prototype.resetValidation = function (idFormGroup) {
    $(idFormGroup)
        .removeClass("has-success")
        .removeClass("has-error");
    $(idFormGroup + " .js-errmsg")
        .addClass("hidden");
};

/**
 * 指定された id の要素に has-success クラスを追加する
 * @param {string} idFormGroup - has-success クラスを追加する要素の id
 */
Form.prototype.setSuccess = function (idFormGroup) {
    $(idFormGroup).addClass("has-success");
};

/**
 * 指定された id の要素に has-error クラスを追加し、エラーメッセージを表示する
 * @param {string} idFormGroup - has-error クラスを追加する要素の id
 * @param {string} errmsg - 表示するエラーメッセージ
 */
Form.prototype.setError = function (idFormGroup, errmsg) {
    $(idFormGroup).addClass("has-error");
    $(idFormGroup + " .js-errmsg").removeClass("hidden");
    $(idFormGroup + " .js-errmsg small").text(errmsg);
};

/**
 * form.idList にセットされている id を全て form.focused にセットして
 * focused イベントが発生したことにする
 * @param {Form} form - Form オブジェクト
 */
Form.prototype.forceAllFocused = function (form) {
    form.idList.forEach(function (id) {
        form.focused[id] = true;
    })
};

/**
 * Form オブジェクトの idList に列挙された id の要素の focus イベントハンドラに
 * Form.focused 配列に id をセットする関数をセットする
 * @param {Form} form - Form オブジェクト
 */
function addFocusEventListener(form) {
    form.idList.forEach(function (id) {
        $(id).on("focus", function (event) {
            form.focused[id] = true;
        })
    })
}

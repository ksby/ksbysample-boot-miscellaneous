"use strict";

module.exports = {

    /**
     * 必須チェック用関数
     * @param {Form} form - Form オブジェクト
     * @param {string} idFormGroup - Validation の SUCCESS/ERROR の結果を反映する要素の id
     * @param {Array} idList - チェックを行う要素の id の配列
     * @param {string} errmsg - チェックエラー時に表示するエラーメッセージ
     */
    checkRequired: function (form, idFormGroup, idList, errmsg) {
        var isValid = !form.isAnyEmpty(idList);
        setSuccessOrError(form, idFormGroup, errmsg, isValid);
    },

    /**
     * ひらがなチェック用関数
     * @param {Form} form - Form オブジェクト
     * @param {string} idFormGroup - Validation の SUCCESS/ERROR の結果を反映する要素の id
     * @param {Array} idList - チェックを行う要素の id の配列
     * @param {string} errmsg - チェックエラー時に表示するエラーメッセージ
     */
    checkHiragana: function (form, idFormGroup, idList, errmsg) {
        var isValid = validateRegexp(idList, "^[\u3041-\u3096]+$");
        setSuccessOrError(form, idFormGroup, errmsg, isValid);
    },

    /**
     * 正規表現チェック用関数
     * @param {Form} form - Form オブジェクト
     * @param {string} idFormGroup - Validation の SUCCESS/ERROR の結果を反映する要素の id
     * @param {Array} idList - チェックを行う要素の id の配列
     * @param {string} pattern - チェックで使用する正規表現のパターン文字列
     * @param {string} errmsg - チェックエラー時に表示するエラーメッセージ
     */
    checkRegexp: function (form, idFormGroup, idList, pattern, errmsg) {
        var isValid = validateRegexp(idList, pattern);
        setSuccessOrError(form, idFormGroup, errmsg, isValid);
    }

};

/**
 * 正規表現チェック用共通関数
 * @param {Array} idList - チェックを行う要素の id の配列
 * @param {string} pattern - チェックで使用する正規表現のパターン文字列
 * @returns {boolean} true:チェックOK, false:チェックNG
 */
function validateRegexp(idList, pattern) {
    var regexp = new RegExp(pattern);
    return idList.reduce(function (p, id) {
        return p + $(id).val();
    }, "").match(regexp);
}

/**
 *
 * @param {Form} form - Form オブジェクト
 * @param {string} idFormGroup - Validation の SUCCESS/ERROR の結果を反映する要素の id
 * @param {string} errmsg - チェックエラー時に表示するエラーメッセージ
 * @param {boolean} isValid - チェック結果
 */
function setSuccessOrError(form, idFormGroup, errmsg, isValid) {
    if (isValid) {
        form.setSuccess(idFormGroup);
    } else {
        form.setError(idFormGroup, errmsg);
        throw new Error(errmsg);
    }
}

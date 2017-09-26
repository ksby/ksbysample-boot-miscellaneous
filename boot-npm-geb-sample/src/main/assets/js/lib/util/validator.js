"use strict";

module.exports = {

    ignoreCheckRequired: false,

    /**
     * 初期状態に戻す
     */
    reset: function () {
        this.ignoreCheckRequired = false;
    },

    /**
     * 必須チェック用関数
     * @param {Form} form - Form オブジェクト
     * @param {string} idFormGroup - Validation の SUCCESS/ERROR の結果を反映する要素の id
     * @param {Array} idList - チェックを行う要素の id の配列
     * @param {string} errmsg - チェックエラー時に表示するエラーメッセージ
     */
    checkRequired: function (form, idFormGroup, idList, errmsg) {
        if (this.ignoreCheckRequired === true) return;
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
        if (form.isAllEmpty(idList)) return;
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
        if (form.isAllEmpty(idList)) return;
        var isValid = validateRegexp(idList, pattern);
        setSuccessOrError(form, idFormGroup, errmsg, isValid);
    },

    /**
     * メールアドレスチェック用関数
     * @param {Form} form - Form オブジェクト
     * @param {string} idFormGroup - Validation の SUCCESS/ERROR の結果を反映する要素の id
     * @param {string} id - チェックを行う要素の id
     * @param {string} errmsg - チェックエラー時に表示するエラーメッセージ
     */
    checkEmail: function (form, idFormGroup, id, errmsg) {
        // 値が入力されていなければチェックしない
        if ($(id).val() === "") return;

        // @で分割して要素数が２つかどうかチェックする
        var elements = $(id).val().split("@");
        var isValid = (elements.length === 2);

        // １つ目及び２つ目の要素に空白、制御文字、非ASCII文字が含まれていないかチェックする
        if (isValid === true) {
            isValid = elements.reduce(function (p, element) {
                return p && element.match(/^[\x21-\x7E]+$/);
            }, true);
        }

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

"use strict";

var $ = require("admin-lte/plugins/jQuery/jquery-2.2.3.min.js");
var moji = require("moji");

module.exports = {

    /**
     * 半角/全角カタナカ → ひらがな変換用関数
     * @param {Array} idList - 変換処理を行う要素の id の配列
     */
    convertHiragana: function (idList) {
        convert(idList, function (id) {
            return moji($(id).val())
                .convert('HK', 'ZK')     // 半角カタナカ → 全角カタカナ
                .convert('KK', 'HG')     // 全角カタカナ → ひらがな
                .toString();
        });
    },

    /**
     * 全角英数 → 半角英数変換用関数
     * @param {Array} idList - 変換処理を行う要素の id の配列
     */
    convertHanAlphaNumeric: function (idList) {
        convert(idList, function (id) {
            return moji($(id).val())
                .convert('ZE', 'HE')     // 全角英数 → 半角英数
                .toString();
        });
    }

};

/**
 * idList で指定された項目の値を convertRuleFunc 関数で変換する
 * @param {Array} idList - 変換処理を行う要素の id の配列
 * @param {Function} convertRuleFunc - 変換関数
 */
function convert(idList, convertRuleFunc) {
    // 変換して値をセットし直すとカーソルのある項目の選択状態が解除されるので、
    // 最初にカーソルのある要素を取得して、セット後に選択状態にし直す
    var $focused = $(":focus");
    idList.forEach(function (id) {
        $(id).val(convertRuleFunc(id));
    });
    $focused.select();
}

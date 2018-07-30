"use strict";

var moji = require("moji");

module.exports = {
  /**
   * 半角/全角カタナカ → ひらがな変換用関数
   * @param {Array} idList - 変換処理を行う要素の id の配列
   */
  convertHiragana: function(idList) {
    convert(idList, function(id) {
      // 半角カタナカ → 全角カタカナ
      // 全角カタカナ → ひらがな
      return moji($(id).val())
        .convert("HK", "ZK")
        .convert("KK", "HG")
        .toString();
    });
  },

  /**
   * 全角英数 → 半角英数変換用関数
   * @param {Array} idList - 変換処理を行う要素の id の配列
   */
  convertHanAlphaNumeric: function(idList) {
    convert(idList, function(id) {
      // 全角英数 → 半角英数
      return moji($(id).val())
        .convert("ZE", "HE")
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
  idList.forEach(function(id) {
    $(id).val(convertRuleFunc(id));
  });
  $focused.select();
}

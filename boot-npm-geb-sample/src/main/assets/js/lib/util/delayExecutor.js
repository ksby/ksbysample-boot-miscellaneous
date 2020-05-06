"use strict";

let id = undefined;

module.exports = {
  /**
   * 指定された時間経過後に実行する関数を登録する
   * @param {Function} fn - 遅延実行する関数
   * @param {number} milliSeconds - 関数 fn を遅延実行する時間（ミリ秒）
   */
  register: function (fn, milliSeconds) {
    if (id !== undefined) {
      this.cancel();
    }
    id = setTimeout(fn, milliSeconds);
  },

  /**
   * 登録されている遅延実行関数をキャンセルする
   */
  cancel: function () {
    if (id !== undefined) {
      clearTimeout(id);
      id = undefined;
    }
  },
};

"use strict";

module.exports = {
  /**
   * 郵便番号検索API を呼び出して、郵便番号から都道府県・市区町村・町域を取得する
   * @param {string} zipcode - 郵便番号（７桁の数字、ハイフン付きでも可）
   */
  search: function (zipcode) {
    var defer = $.Deferred();
    $.ajax({
      type: "get",
      url: "http://zipcloud.ibsnet.co.jp/api/search",
      data: { zipcode: zipcode },
      cache: false,
      dataType: "jsonp",
      timeout: 15000,
      success: defer.resolve,
      error: defer.reject,
    });
    return defer.promise();
  },
};

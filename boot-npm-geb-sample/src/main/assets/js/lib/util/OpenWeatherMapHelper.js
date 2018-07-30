"use strict";

var axios = require("axios");
var openWeatherMapKey = require("lib/util/OpenWeatherMap.key.js");

var DEFAUL_TIMEOUT = 15000;
var timeout = DEFAUL_TIMEOUT;

module.exports = {
  /**
   * 内部の設定を初期値に戻す
   */
  init: function() {
    timeout = DEFAUL_TIMEOUT;
  },

  /**
   * タイムアウト時間を設定する
   * @param milliseconds
   */
  setTimeout: function(milliseconds) {
    timeout = milliseconds;
  },

  /**
   * 指定された都市の現在の天気情報を取得する
   * @param {string} cityName - 都市名
   */
  getCurrentWeatherDataByCityName: function(cityName) {
    return getAxiosBase().get("/data/2.5/weather", {
      params: {
        q: cityName,
        appid: openWeatherMapKey.appid
      }
    });
  }
};

/**
 * 共通設定が定義済の Axios オブジェクトを返す
 * @returns {Axios} OpenWeatherMap の API を呼び出す時の共通設定が定義済の Axios オブジェクト
 */
function getAxiosBase() {
  return axios.create({
    baseURL: "http://api.openweathermap.org",
    timeout: timeout
  });
}

"use strict";

global.$ = require("jquery");
const delayExecutor = require("lib/util/delayExecutor.js");

jest.useFakeTimers();

describe("delayExecutor.js のテスト", () => {
  function setSampleValue() {
    $("#sample").val("サンプル");
  }

  function setTestValue() {
    $("#sample").val("テスト");
  }

  beforeEach(() => {
    document.body.innerHTML = `
            <input type="text" name="sample" id="sample" value="">
        `;
  });

  test("delayExecutor.register で関数を登録すると指定した時間経過後に実行される", () => {
    delayExecutor.register(setSampleValue, 2000);
    expect($("#sample").val()).toBe("");
    jest.runAllTimers();
    expect($("#sample").val()).toBe("サンプル");
  });

  test("delayExecutor.cancel を呼び出せば指定した時間を経過しても実行されない", () => {
    delayExecutor.register(setSampleValue, 2000);

    // ここで jest.advanceTimersByTime(...) を呼び出して、
    // 少しだけ時間を経過させたかった。。。

    delayExecutor.cancel();
    expect($("#sample").val()).toBe("");
    jest.runAllTimers();
    expect($("#sample").val()).toBe("");
  });

  test("delayExecutor.register は最後に登録した関数だけが実行される", () => {
    delayExecutor.register(setSampleValue, 2000);
    delayExecutor.register(setTestValue, 1000);

    expect($("#sample").val()).toBe("");
    jest.runAllTimers();
    expect($("#sample").val()).toBe("テスト");
  });
});

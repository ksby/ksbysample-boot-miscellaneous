"use strict";

global.$ = require("jquery");
const converter = require("lib/util/converter.js");

describe("converter.js のテスト", () => {
  describe("convertHiragana のテスト", () => {
    beforeEach(() => {
      document.body.innerHTML = `
              <input type="text" name="sample" id="sample" value=""/>
            `;
    });

    test("全角カタカナはひらがなに変更される", () => {
      $("#sample").val("アイウエオ");
      converter.convertHiragana(["#sample"]);
      expect($("#sample").val()).toBe("あいうえお");
    });

    test("半角カタカナはひらがなに変更される", () => {
      $("#sample").val("ｱｲｳｴｵ");
      converter.convertHiragana(["#sample"]);
      expect($("#sample").val()).toBe("あいうえお");
    });
  });

  describe("convertHiragana のテスト(Jest Each版)", () => {
    beforeEach(() => {
      document.body.innerHTML = `
              <input type="text" name="sample" id="sample" value=""/>
            `;
    });

    test.each`
      str             | expected
      ${"アイウエオ"} | ${"あいうえお"}
      ${"ｱｲｳｴｵ"}      | ${"あいうえおx"}
    `("$str --> $expected", ({ str, expected }) => {
      $("#sample").val(str);
      converter.convertHiragana(["#sample"]);
      expect($("#sample").val()).toBe(expected);
    });
  });

  describe("convertHanAlphaNumeric のテスト", () => {
    beforeEach(() => {
      document.body.innerHTML = `
              <input type="text" name="sample" id="sample" value=""/>
            `;
    });

    test("全角英数字は半角に変更される", () => {
      $("#sample").val("ＡＺａｚ０９");
      converter.convertHanAlphaNumeric(["#sample"]);
      expect($("#sample").val()).toBe("AZaz09");
    });
  });
});

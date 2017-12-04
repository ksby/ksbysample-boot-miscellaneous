"use strict";

global.$ = require("jquery");
const validator = require("lib/util/validator.js");
// Form クラスをモックにする
jest.mock("lib/class/Form.js");
const Form = require("lib/class/Form.js");

describe("validator.js のテスト", () => {

    describe("checkRequired のテスト", () => {
        test("form.isAnyEmpty = false なら form.setSuccess が呼び出される", () => {
            // form.isAnyEmpty をモックメソッドにして常に false を返すようにする
            var form = new Form([]);
            form.isAnyEmpty = jest.fn().mockImplementation(() => false);

            // validator.checkRequired メソッドを呼び出す
            validator.checkRequired(form, ["#form-group-sample"], ["#sample"], "");

            // form.setSuccess メソッドが呼び出され、form.setError メソッドが呼び出されていないことを確認する
            expect(form.setSuccess).toBeCalled();
            expect(form.setError).not.toBeCalled();
        });

        test("form.isAnyEmpty = true なら form.setError が呼び出される", () => {
            // form.isAnyEmpty をモックメソッドにして常に true を返すようにする
            var form = new Form([]);
            form.isAnyEmpty = jest.fn().mockImplementation(() => true);

            // validator.checkRequired メソッドを呼び出す
            // Error オブジェクトが throw されるはず
            const errmsg = "エラーメッセージ";
            expect(() => {
                validator.checkRequired(form, ["#form-group-sample"], ["#sample"], errmsg);
            }).toThrow(new Error(errmsg));

            // form.setSuccess メソッドは呼び出されず、form.setError メソッドが呼び出されることを確認する
            expect(form.setSuccess).not.toBeCalled();
            expect(form.setError).toBeCalled();
        });
    });

});

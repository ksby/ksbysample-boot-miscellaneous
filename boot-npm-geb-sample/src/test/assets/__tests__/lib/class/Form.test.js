"use strict";

global.$ = require("jquery");
const Form = require("lib/class/Form.js");

describe("Form.js のテスト", () => {

    describe("focus イベントに関するメソッドのテスト", () => {

        beforeEach(() => {
            document.body.innerHTML = `
              <input type="text" name="name" id="name" value=""/>
              <input type="radio" name="age" id="age_1" value="1"/>男性
              <input type="radio" name="age" id="age_2" value="2"/>女性
              <input type="checkbox" name="enquete" id="enquete_1" value="1"/>回答１
              <input type="checkbox" name="enquete" id="enquete_2" value="2"/>回答２
              <select id="job">
                <option value="1">会社員</option>
                <option value="2">学生</option>
                <option value="3">その他</option>
              </select>
            `;
        });

        const idList = [
            "#name",
            "input:radio[name='age']",
            "input:checkbox[name='enquete']",
            "#job"
        ];

        test("focusイベントが発生するとfocusedプロパティにselectorがセットされる", () => {
            let form = new Form(idList);
            expect(form.focused).not.toHaveProperty("#name", true);
            expect(form.focused).not.toHaveProperty("input:radio[name='age']", true);
            expect(form.focused).not.toHaveProperty("input:checkbox[name='enquete']", true);
            expect(form.focused).not.toHaveProperty("#job", true);

            $("#name").focus();
            expect(form.focused).toHaveProperty("#name", true);
            expect(form.focused).not.toHaveProperty("input:radio[name='age']", true);
            expect(form.focused).not.toHaveProperty("input:checkbox[name='enquete']", true);
            expect(form.focused).not.toHaveProperty("#job", true);

            $("input:radio[name='age']").focus();
            expect(form.focused).toHaveProperty("#name", true);
            expect(form.focused).toHaveProperty("input:radio[name='age']", true);
            expect(form.focused).not.toHaveProperty("input:checkbox[name='enquete']", true);
            expect(form.focused).not.toHaveProperty("#job", true);

            $("input:checkbox[name='enquete']").focus();
            expect(form.focused).toHaveProperty("#name", true);
            expect(form.focused).toHaveProperty("input:radio[name='age']", true);
            expect(form.focused).toHaveProperty("input:checkbox[name='enquete']", true);
            expect(form.focused).not.toHaveProperty("#job", true);

            $("#job").focus();
            expect(form.focused).toHaveProperty("#name", true);
            expect(form.focused).toHaveProperty("input:radio[name='age']", true);
            expect(form.focused).toHaveProperty("input:checkbox[name='enquete']", true);
            expect(form.focused).toHaveProperty("#job", true);
        });

        test("isAllFocused メソッドのテスト", () => {
            const idList2 = [
                "input:radio[name='age']",
                "input:checkbox[name='enquete']"
            ];
            let form = new Form(idList);
            expect(form.isAllFocused(form, idList)).toBe(false);

            $("#name").focus();
            expect(form.isAllFocused(form, idList)).toBe(false);

            $("input:radio[name='age']").focus();
            expect(form.isAllFocused(form, idList)).toBe(false);
            expect(form.isAllFocused(form, idList2)).toBe(false);

            $("input:checkbox[name='enquete']").focus();
            expect(form.isAllFocused(form, idList)).toBe(false);
            expect(form.isAllFocused(form, idList2)).toBe(true);

            $("#job").focus();
            expect(form.isAllFocused(form, idList)).toBe(true);
        });

        test("setFocusedFromList メソッドのテスト", () => {
            let form = new Form(idList);
            expect(form.isAllFocused(form, idList)).toBe(false);
            form.setFocusedFromList(form, idList);
            expect(form.isAllFocused(form, idList)).toBe(true);

            const idList2 = [
                "input:radio[name='age']",
                "input:checkbox[name='enquete']"
            ];
            let form2 = new Form(idList);
            expect(form2.isAllFocused(form2, idList)).toBe(false);
            form2.setFocusedFromList(form2, idList2);
            expect(form2.isAllFocused(form2, idList)).toBe(false);
            expect(form2.isAllFocused(form2, idList2)).toBe(true);
        });

        test("forceAllFocused メソッドのテスト", () => {
            let form = new Form(idList);
            expect(form.isAllFocused(form, idList)).toBe(false);
            form.forceAllFocused(form);
            expect(form.isAllFocused(form, idList)).toBe(true);
        });

        test("backupFocusedState, restoreFocusedState メソッドのテスト", () => {
            let form = new Form(idList);
            form.forceAllFocused(form);
            expect(form.focused).toHaveProperty("#name", true);
            expect(form.focused).toHaveProperty("input:radio[name='age']", true);
            expect(form.focused).toHaveProperty("input:checkbox[name='enquete']", true);
            expect(form.focused).toHaveProperty("#job", true);
            expect(form.backupFocused).not.toHaveProperty("#name", true);
            expect(form.backupFocused).not.toHaveProperty("input:radio[name='age']", true);
            expect(form.backupFocused).not.toHaveProperty("input:checkbox[name='enquete']", true);
            expect(form.backupFocused).not.toHaveProperty("#job", true);

            form.backupFocusedState(form);
            expect(form.backupFocused).toHaveProperty("#name", true);
            expect(form.backupFocused).toHaveProperty("input:radio[name='age']", true);
            expect(form.backupFocused).toHaveProperty("input:checkbox[name='enquete']", true);
            expect(form.backupFocused).toHaveProperty("#job", true);

            form.focused = {};
            form.restoreFocusedState(form);
            expect(form.focused).toHaveProperty("#name", true);
            expect(form.focused).toHaveProperty("input:radio[name='age']", true);
            expect(form.focused).toHaveProperty("input:checkbox[name='enquete']", true);
            expect(form.focused).toHaveProperty("#job", true);
        });

    });

    describe("値の入力有無をチェックするメソッドのテストを書く", () => {

        beforeEach(() => {
            document.body.innerHTML = `
              <input type="text" name="name" id="name" value=""/>
              <input type="radio" name="age" id="age_1" value="1"/>男性
              <input type="radio" name="age" id="age_2" value="2"/>女性
              <input type="checkbox" name="enquete" id="enquete_1" value="1"/>回答１
              <input type="checkbox" name="enquete" id="enquete_2" value="2"/>回答２
              <select id="job">
                <option value="">選択してください</option>
                <option value="1">会社員</option>
                <option value="2">学生</option>
                <option value="3">その他</option>
              </select>
            `;
        });

        const idList = [
            "#name",
            "input:radio[name='age']",
            "input:checkbox[name='enquete']",
            "#job"
        ];

        test("isAllEmpty メソッドのテスト", () => {
            let form = new Form(idList);
            expect(form.isAllEmpty(idList)).toBe(true);

            // input[type='text']
            $("#name").val("a");
            expect(form.isAllEmpty(idList)).toBe(false);
            $("#name").val("");
            expect(form.isAllEmpty(idList)).toBe(true);

            // input[type='radio']
            $("input:radio[name='age'][value='1']").prop("checked", true);
            expect(form.isAllEmpty(idList)).toBe(false);
            $("input:radio[name='age'][value='1']").prop("checked", false);
            expect(form.isAllEmpty(idList)).toBe(true);
            $("input:radio[name='age'][value='2']").prop("checked", true);
            expect(form.isAllEmpty(idList)).toBe(false);
            $("input:radio[name='age'][value='2']").prop("checked", false);
            expect(form.isAllEmpty(idList)).toBe(true);

            // input[type='checkbox']
            $("input:checkbox[name='enquete'][value='1']").prop("checked", true);
            expect(form.isAllEmpty(idList)).toBe(false);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", false);
            expect(form.isAllEmpty(idList)).toBe(true);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", true);
            expect(form.isAllEmpty(idList)).toBe(false);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", false);
            expect(form.isAllEmpty(idList)).toBe(true);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", true);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", true);
            expect(form.isAllEmpty(idList)).toBe(false);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", false);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", false);
            expect(form.isAllEmpty(idList)).toBe(true);

            // select
            $("#job").val("1");
            expect(form.isAllEmpty(idList)).toBe(false);
            $("#job").val("2");
            expect(form.isAllEmpty(idList)).toBe(false);
            $("#job").val("3");
            expect(form.isAllEmpty(idList)).toBe(false);
            $("#job").val("");
            expect(form.isAllEmpty(idList)).toBe(true);
        });

        test("isAnyEmpty メソッドのテスト", () => {
            let form = new Form(idList);
            expect(form.isAnyEmpty(idList)).toBe(true);

            $("#name").val("a");
            $("input:radio[name='age'][value='1']").prop("checked", true);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", true);
            $("#job").val("1");
            expect(form.isAnyEmpty(idList)).toBe(false);

            $("#name").val("");
            expect(form.isAnyEmpty(idList)).toBe(true);
            $("#name").val("a");
            expect(form.isAnyEmpty(idList)).toBe(false);

            $("input:radio[name='age'][value='1']").prop("checked", false);
            expect(form.isAnyEmpty(idList)).toBe(true);
            $("input:radio[name='age'][value='1']").prop("checked", true);
            expect(form.isAnyEmpty(idList)).toBe(false);

            $("input:checkbox[name='enquete'][value='1']").prop("checked", false);
            expect(form.isAnyEmpty(idList)).toBe(true);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", true);
            expect(form.isAnyEmpty(idList)).toBe(false);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", true);
            expect(form.isAnyEmpty(idList)).toBe(false);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", false);
            expect(form.isAnyEmpty(idList)).toBe(false);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", false);
            expect(form.isAnyEmpty(idList)).toBe(true);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", true);

            $("#job").val("");
            expect(form.isAnyEmpty(idList)).toBe(true);
            $("#job").val("1");
            expect(form.isAnyEmpty(idList)).toBe(false);
        });

        test("isAnyNotEmpty メソッドのテスト", () => {
            let form = new Form(idList);
            expect(form.isAnyNotEmpty(idList)).toBe(false);

            // input[type='text']
            $("#name").val("a");
            expect(form.isAnyNotEmpty(idList)).toBe(true);
            $("#name").val("");
            expect(form.isAnyNotEmpty(idList)).toBe(false);

            $("input:radio[name='age'][value='1']").prop("checked", true);
            expect(form.isAnyNotEmpty(idList)).toBe(true);
            $("input:radio[name='age'][value='1']").prop("checked", false);
            expect(form.isAnyNotEmpty(idList)).toBe(false);

            $("input:checkbox[name='enquete'][value='1']").prop("checked", true);
            expect(form.isAnyNotEmpty(idList)).toBe(true);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", false);
            expect(form.isAnyNotEmpty(idList)).toBe(false);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", true);
            expect(form.isAnyNotEmpty(idList)).toBe(true);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", false);
            expect(form.isAnyNotEmpty(idList)).toBe(false);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", true);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", true);
            expect(form.isAnyNotEmpty(idList)).toBe(true);
            $("input:checkbox[name='enquete'][value='1']").prop("checked", false);
            $("input:checkbox[name='enquete'][value='2']").prop("checked", false);
            expect(form.isAnyNotEmpty(idList)).toBe(false);

            $("#job").val("1");
            expect(form.isAnyNotEmpty(idList)).toBe(true);
            $("#job").val("");
            expect(form.isAnyNotEmpty(idList)).toBe(false);
        });

    });

});

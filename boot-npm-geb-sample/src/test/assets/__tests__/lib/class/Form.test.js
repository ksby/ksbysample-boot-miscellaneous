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
      "#job",
    ];

    test("focusイベントが発生するとfocusedプロパティにselectorがセットされる", () => {
      let form = new Form(idList);
      expect(form.focused).not.toHaveProperty("#name", true);
      expect(form.focused).not.toHaveProperty("input:radio[name='age']", true);
      expect(form.focused).not.toHaveProperty(
        "input:checkbox[name='enquete']",
        true
      );
      expect(form.focused).not.toHaveProperty("#job", true);

      $("#name").focus();
      expect(form.focused).toHaveProperty("#name", true);
      expect(form.focused).not.toHaveProperty("input:radio[name='age']", true);
      expect(form.focused).not.toHaveProperty(
        "input:checkbox[name='enquete']",
        true
      );
      expect(form.focused).not.toHaveProperty("#job", true);

      $("input:radio[name='age']").focus();
      expect(form.focused).toHaveProperty("#name", true);
      expect(form.focused).toHaveProperty("input:radio[name='age']", true);
      expect(form.focused).not.toHaveProperty(
        "input:checkbox[name='enquete']",
        true
      );
      expect(form.focused).not.toHaveProperty("#job", true);

      $("input:checkbox[name='enquete']").focus();
      expect(form.focused).toHaveProperty("#name", true);
      expect(form.focused).toHaveProperty("input:radio[name='age']", true);
      expect(form.focused).toHaveProperty(
        "input:checkbox[name='enquete']",
        true
      );
      expect(form.focused).not.toHaveProperty("#job", true);

      $("#job").focus();
      expect(form.focused).toHaveProperty("#name", true);
      expect(form.focused).toHaveProperty("input:radio[name='age']", true);
      expect(form.focused).toHaveProperty(
        "input:checkbox[name='enquete']",
        true
      );
      expect(form.focused).toHaveProperty("#job", true);
    });

    test("isAllFocused メソッドのテスト", () => {
      const idList2 = [
        "input:radio[name='age']",
        "input:checkbox[name='enquete']",
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
        "input:checkbox[name='enquete']",
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
      expect(form.focused).toHaveProperty(
        "input:checkbox[name='enquete']",
        true
      );
      expect(form.focused).toHaveProperty("#job", true);
      expect(form.backupFocused).not.toHaveProperty("#name", true);
      expect(form.backupFocused).not.toHaveProperty(
        "input:radio[name='age']",
        true
      );
      expect(form.backupFocused).not.toHaveProperty(
        "input:checkbox[name='enquete']",
        true
      );
      expect(form.backupFocused).not.toHaveProperty("#job", true);

      form.backupFocusedState(form);
      expect(form.backupFocused).toHaveProperty("#name", true);
      expect(form.backupFocused).toHaveProperty(
        "input:radio[name='age']",
        true
      );
      expect(form.backupFocused).toHaveProperty(
        "input:checkbox[name='enquete']",
        true
      );
      expect(form.backupFocused).toHaveProperty("#job", true);

      form.focused = {};
      form.restoreFocusedState(form);
      expect(form.focused).toHaveProperty("#name", true);
      expect(form.focused).toHaveProperty("input:radio[name='age']", true);
      expect(form.focused).toHaveProperty(
        "input:checkbox[name='enquete']",
        true
      );
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
      "#job",
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

  describe("class 属性を変更するメソッドのテスト", () => {
    describe("resetValidation メソッドのテスト", () => {
      test("入力チェック成功時(has-success)は has-success が削除される", () => {
        document.body.innerHTML = `
                    <div class="form-group has-success" id="form-group-name">
                      <div class="control-label col-sm-2">
                        <label class="float-label">お名前</label>
                      </div>
                      <div class="col-sm-10">
                        <div class="row"><div class="col-sm-10">
                          <input type="text" name="lastname" id="lastname" class="form-control form-control-inline"
                                value="" placeholder="例）田中"/>
                        </div></div>
                        <div class="row hidden js-errmsg"><div class="col-sm-10">
                          <p class="form-control-static text-danger"><small>ここにエラーメッセージを表示します</small></p>
                        </div></div>
                      </div>
                    </div>
                `;

        let form = new Form([]);
        expect($("#form-group-name").hasClass("has-success")).toBe(true);
        expect($(".js-errmsg").hasClass("hidden")).toBe(true);
        form.resetValidation("#form-group-name");
        expect($("#form-group-name").hasClass("has-success")).toBe(false);
        expect($(".js-errmsg").hasClass("hidden")).toBe(true);
      });

      test("入力チェックエラー時(has-error)は has-error が削除されてエラーメッセージが非表示になる", () => {
        document.body.innerHTML = `
                    <div class="form-group has-error" id="form-group-name">
                      <div class="control-label col-sm-2">
                        <label class="float-label">お名前</label>
                      </div>
                      <div class="col-sm-10">
                        <div class="row"><div class="col-sm-10">
                          <input type="text" name="lastname" id="lastname" class="form-control form-control-inline"
                                value="" placeholder="例）田中"/>
                        </div></div>
                        <div class="row js-errmsg"><div class="col-sm-10">
                          <p class="form-control-static text-danger"><small>ここにエラーメッセージを表示します</small></p>
                        </div></div>
                      </div>
                    </div>
                `;

        let form = new Form([]);
        expect($("#form-group-name").hasClass("has-error")).toBe(true);
        expect($(".js-errmsg").hasClass("hidden")).toBe(false);
        form.resetValidation("#form-group-name");
        expect($("#form-group-name").hasClass("has-error")).toBe(false);
        expect($(".js-errmsg").hasClass("hidden")).toBe(true);
      });
    });

    test("setSuccess メソッドのテスト", () => {
      document.body.innerHTML = `
                <div class="form-group" id="form-group-name">
                </div>
            `;

      let form = new Form([]);
      expect($("#form-group-name").hasClass("has-success")).toBe(false);
      form.setSuccess("#form-group-name");
      expect($("#form-group-name").hasClass("has-success")).toBe(true);
    });

    test("setError メソッドのテスト", () => {
      document.body.innerHTML = `
                <div class="form-group" id="form-group-name">
                  <div class="control-label col-sm-2">
                    <label class="float-label">お名前</label>
                  </div>
                  <div class="col-sm-10">
                    <div class="row"><div class="col-sm-10">
                      <input type="text" name="lastname" id="lastname" class="form-control form-control-inline"
                            value="" placeholder="例）田中"/>
                    </div></div>
                    <div class="row hidden js-errmsg"><div class="col-sm-10">
                      <p class="form-control-static text-danger"><small>ここにエラーメッセージを表示します</small></p>
                    </div></div>
                  </div>
                </div>
            `;

      let form = new Form([]);
      expect($("#form-group-name").hasClass("has-error")).toBe(false);
      expect($(".js-errmsg").hasClass("hidden")).toBe(true);
      expect($(".js-errmsg small").text()).toBe(
        "ここにエラーメッセージを表示します"
      );
      form.setError("#form-group-name", "お名前を入力してください");
      expect($("#form-group-name").hasClass("has-error")).toBe(true);
      expect($(".js-errmsg").hasClass("hidden")).toBe(false);
      expect($(".js-errmsg small").text()).toBe("お名前を入力してください");
    });
  });

  describe("Validation 用メソッドのテスト", () => {
    describe("convertAndValidate メソッドのテスト", () => {
      const idFormGroup = "#form-group-name";
      const idList = ["#name", "#age"];

      test("全ての要素に focus していると converter, validator が呼ばれる", () => {
        let form = new Form(idList);
        let event = $.Event("test");
        const converter = jest.fn();
        const validator = jest.fn();

        form.forceAllFocused(form);
        form.convertAndValidate(
          form,
          event,
          idFormGroup,
          idList,
          converter,
          validator
        );
        expect(converter).toBeCalled();
        expect(validator).toBeCalled();
        expect(event.isPropagationStopped()).toBe(false);
      });

      test("全ての要素に focus していないと converter, validator は呼ばれない", () => {
        let form = new Form(idList);
        let event = $.Event("test");
        const converter = jest.fn();
        const validator = jest.fn();

        form.convertAndValidate(
          form,
          event,
          idFormGroup,
          idList,
          converter,
          validator
        );
        expect(converter).not.toBeCalled();
        expect(validator).not.toBeCalled();
        expect(event.isPropagationStopped()).toBe(false);
      });

      test("converter, validator に undefined を渡してもエラーにならない", () => {
        let form = new Form(idList);
        let event = $.Event("test");

        form.forceAllFocused(form);
        expect(() => {
          form.convertAndValidate(
            form,
            event,
            idFormGroup,
            idList,
            undefined,
            undefined
          );
        }).not.toThrow();
        expect(event.isPropagationStopped()).toBe(false);
      });

      test("validator が Error オブジェクトを throw すると event.stopPropagation() が呼ばれる", () => {
        let form = new Form(idList);
        let event = $.Event("test");
        const converter = jest.fn();
        const validatorThrowError = jest.fn().mockImplementation(() => {
          throw new Error();
        });

        form.forceAllFocused(form);
        form.convertAndValidate(
          form,
          event,
          idFormGroup,
          idList,
          converter,
          validatorThrowError
        );
        expect(event.isPropagationStopped()).toBe(true);
      });
    });
  });
});

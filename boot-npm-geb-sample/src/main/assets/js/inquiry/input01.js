"use strict";

var Form = require("lib/class/Form.js");
var converter = require("lib/util/converter.js");
var validator = require("lib/util/validator.js");
require("vendor/autokana/jquery.autoKana.js");

var form = new Form([
  "#lastname",
  "#firstname",
  "#lastkana",
  "#firstkana",
  "input:radio[name='sex']",
  "#age",
  "#job",
]);

var nameValidator = function (event) {
  var idFormGroup = "#form-group-name";
  var idList = ["#lastname", "#firstname"];
  form.convertAndValidate(
    form,
    event,
    idFormGroup,
    idList,
    undefined,
    function () {
      validator.checkRequired(
        form,
        idFormGroup,
        idList,
        "お名前（漢字）を入力してください"
      );
    }
  );
};

var kanaValidator = function (event) {
  var idFormGroup = "#form-group-kana";
  var idList = ["#lastkana", "#firstkana"];
  form.convertAndValidate(
    form,
    event,
    idFormGroup,
    idList,
    function () {
      converter.convertHiragana(idList);
    },
    function () {
      validator.checkRequired(
        form,
        idFormGroup,
        idList,
        "お名前（かな）を入力してください"
      );
      validator.checkHiragana(
        form,
        idFormGroup,
        idList,
        "お名前（かな）にはひらがなを入力してください"
      );
    }
  );
};

var sexValidator = function (event) {
  var idFormGroup = "#form-group-sex";
  var idList = ["input:radio[name='sex']"];
  form.convertAndValidate(
    form,
    event,
    idFormGroup,
    idList,
    undefined,
    function () {
      validator.checkRequired(
        form,
        idFormGroup,
        idList,
        "性別を選択してください"
      );
    }
  );
};

var ageValidator = function (event) {
  var idFormGroup = "#form-group-age";
  var idList = ["#age"];
  form.convertAndValidate(
    form,
    event,
    idFormGroup,
    idList,
    function () {
      converter.convertHanAlphaNumeric(idList);
    },
    function () {
      validator.checkRequired(
        form,
        idFormGroup,
        idList,
        "年齢を入力してください"
      );
      validator.checkRegexp(
        form,
        idFormGroup,
        idList,
        "^[0-9]+$",
        "年齢には数字を入力してください"
      );
    }
  );
};

var jobValidator = function () {
  var idFormGroup = "#form-group-job";
  form.setSuccess(idFormGroup);
};

var kanaAutoInputValidator = function (event) {
  var idList = ["#lastkana", "#firstkana"];
  // 「お名前（漢字）」を入力して「お名前（かな）」が自動入力された時に、
  // 「お名前（かな）」の入力チェックが実行されるようにする
  if (!form.isAnyEmpty(idList)) {
    form.setFocusedFromList(form, idList);
    kanaValidator(event);
  }
};

var executeAllValidator = function (event) {
  form.forceAllFocused(form);
  [
    nameValidator,
    kanaValidator,
    sexValidator,
    ageValidator,
    jobValidator,
  ].forEach(function (validateFunction) {
    validateFunction(event);
  });
};

var btnNextClickHandler = function (event) {
  // 全ての入力チェックを実行する
  executeAllValidator(event);
  // 入力チェックエラーがある場合には処理を中断する
  if (event.isPropagationStopped()) {
    // 一番最初のエラーの項目にカーソルを移動する
    $(".has-error:first :input:first").focus().select();
    return false;
  }

  // 「次へ」ボタンをクリック不可にする
  $(".js-btn-next").prop("disabled", true);

  // サーバにリクエストを送信する
  $("#inquiryInput01Form").attr("action", "/inquiry/input/01/?move=next");
  $("#inquiryInput01Form").submit();

  // return false は
  // event.preventDefault() + event.stopPropagation() らしい
  return false;
};

function delStringExceedingMaxlength(id) {
  $(id).val($(id).val().substring(0, $(id).attr("maxlength")));
}

$(document).ready(function (event) {
  // 「お名前（漢字）」が入力された時に、かな文字列を「お名前（かな）」に自動入力されるようにする
  $.fn.autoKana("#lastname", "#lastkana");
  $.fn.autoKana("#firstname", "#firstkana");

  // autokana で自動入力されると maxlength の文字数を超える文字が自動入力される場合があるので
  // maxlegnth の文字数を超えた分を削除する
  $("#lastname").on("keyup", function () {
    delStringExceedingMaxlength("#lastkana");
  });
  $("#firstname").on("keyup", function () {
    delStringExceedingMaxlength("#firstkana");
  });

  // 入力チェック用の validator 関数をセットする
  $("#lastname").on("blur", nameValidator).on("blur", kanaAutoInputValidator);
  $("#firstname").on("blur", nameValidator).on("blur", kanaAutoInputValidator);
  $("#lastkana").on("blur", kanaValidator);
  $("#firstkana").on("blur", kanaValidator);
  $("input:radio[name='sex']").on("blur", sexValidator);
  $("#age").on("blur", ageValidator);
  $("#job").on("blur", jobValidator);

  // 「次へ」ボタンクリック時の処理をセットする
  $(".js-btn-next").on("click", btnNextClickHandler);

  // 初期画面表示時にセッションに保存されていたデータを表示する場合には
  // 入力チェックを実行して画面の表示を入力チェック後の状態にする
  if ($("#copiedFromSession").val() === "true") {
    executeAllValidator(event);
  }

  // 「お名前（漢字）」の「姓」にフォーカスをセットする
  $("#lastname").focus().select();
});

var $ = require("admin-lte/plugins/jQuery/jquery-2.2.3.min.js");

$(document).ready(function () {
    // 動作確認のために初期表示時に「確認画面へ」ボタンをクリック可能にする
    $(".js-btn-confirm").prop("disabled", false);

    $(".js-btn-back").on("click", function (event) {
        $("#input03Form").attr("action", "/inquiry/input/03/?move=back");
        $("#input03Form").submit();
        return false;
    })

    $(".js-btn-confirm").on("click", function (event) {
        $("#input03Form").attr("action", "/inquiry/input/03/?move=next");
        $("#input03Form").submit();
        return false;
    })
});

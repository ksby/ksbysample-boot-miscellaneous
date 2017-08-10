var $ = require("admin-lte/plugins/jQuery/jquery-2.2.3.min.js");

$(document).ready(function () {
    // 動作確認のために初期表示時に「次へ」ボタンをクリック可能にする
    $(".js-btn-next").prop("disabled", false);

    $(".js-btn-next").on("click", function (event) {
        $("#input01Form").attr("action", "/inquiry/input/01/?move=next");
        $("#input01Form").submit();

        // return false は
        // event.preventDefault() + event.stopPropagation() らしい
        return false;
    })
});

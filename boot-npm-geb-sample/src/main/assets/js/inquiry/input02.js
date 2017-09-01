$(document).ready(function () {
    // 動作確認のために初期表示時に「次へ」ボタンをクリック可能にする
    $(".js-btn-next").prop("disabled", false);

    $(".js-btn-back").on("click", function (event) {
        $("#input02Form").attr("action", "/inquiry/input/02/?move=back");
        $("#input02Form").submit();
        return false;
    })

    $(".js-btn-next").on("click", function (event) {
        $("#input02Form").attr("action", "/inquiry/input/02/?move=next");
        $("#input02Form").submit();
        return false;
    })
});

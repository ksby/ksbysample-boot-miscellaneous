var $ = require("admin-lte/plugins/jQuery/jquery-2.2.3.min.js");

$(document).ready(function () {
    $(".js-btn-input01").on("click", function (event) {
        location.href = "/inquiry/input/01/";
        return false;
    });

    $(".js-btn-input02").on("click", function (event) {
        location.href = "/inquiry/input/02/";
        return false;
    });

    $(".js-btn-input03").on("click", function (event) {
        location.href = "/inquiry/input/03/";
        return false;
    });

    $(".js-btn-send").on("click", function (event) {
        $("#confirmForm").attr("action", "/inquiry/confirm/send/");
        $("#confirmForm").submit();
        return false;
    });
});

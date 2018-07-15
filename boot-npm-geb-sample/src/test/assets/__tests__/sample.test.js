// 別ファイルの jQuery を利用したモジュールをテストする場合に var $ = require("jquery") だと
// エラーになるので global.$ にセットする
global.$ = require("jquery");

describe("テストのサンプルその１", () => {
  const setError = function(id) {
    $(id).addClass("has-error");
  };

  const sampleBlurEventHandler = function(event) {
    $("#sample").val("length = " + $("#sample").val().length);
  };

  beforeEach(() => {
    // HTML を書く時は ES2015 のテンプレート文字列を使うと楽
    document.body.innerHTML = `
            <div class="form-group" id="form-group-sample">
              <div class="control-label col-sm-2">
                <label class="float-label">サンプル</label>
              </div>
              <div class="col-sm-10">
                <div class="row">
                  <div class="col-sm-10">
                    <input type="text" name="sample" id="sample" class="form-control form-control-inline"
                           maxlength="20" value="" placeholder="例）サンプル"/>
                  </div>
                </div>
                <div class="row hidden js-errmsg">
                  <div class="col-sm-10">
                    <p class="form-control-static text-danger">
                      <small>ここにエラーメッセージを表示します</small>
                    </p>
                  </div>
                </div>
              </div>
            </div>
        `;
  });

  test("setError関数を呼ぶと指定されたidのelementにhas-errorクラスが追加される", () => {
    expect($("#form-group-sample").prop("class")).not.toContain("has-error");
    setError("#form-group-sample");
    expect($("#form-group-sample").prop("class")).toContain("has-error");
  });

  test("blurイベントが発生すると値が'length = [入力された文字列の長さ]'に変わる", () => {
    const str = "これはテストです。";
    $("#sample").on("blur", sampleBlurEventHandler);
    $("#sample").val(str);
    $("#sample").blur();
    expect($("#sample").val()).toBe("length = " + str.length);
  });
});

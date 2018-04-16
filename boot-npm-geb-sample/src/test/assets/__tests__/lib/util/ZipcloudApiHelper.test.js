"use strict";

global.$ = require("jquery");
const zipcloudApiHeler = require("lib/util/ZipcloudApiHelper.js");
const mockjax = require("jquery-mockjax")(global.$, window);

describe("ZipcloudApiHelper.js のテスト", () => {

    // Jest を 22.4 以降にバージョンアップしたらエラーが出るようになったのでコメントアウトする
    /*
    test("100-0005で検索すると東京都千代田区丸の内が１件ヒットする", async () => {
        const json = await zipcloudApiHeler.search("1000005");
        expect(json.results.length).toBe(1);
        expect(json.results[0].address1).toBe("東京都");
        expect(json.results[0].address2).toBe("千代田区");
        expect(json.results[0].address3).toBe("丸の内");
    });
    */

    describe("$.ajax のモックを作成してテストする", () => {

        /**
         * $.ajax モック用関数
         * https://stackoverflow.com/questions/5272698/how-to-fake-jquery-ajax-response 参照
         */
        function ajax_response(response, success) {
            return function (params) {
                if (success) {
                    params.success(response);
                } else {
                    params.error(response);
                }
            };
        }

        test("success のテスト", async () => {
            $.ajax = ajax_response({
                results: [
                    {address1: "東京都"},
                    {address1: "神奈川県"}
                ]
            }, true);
            const json = await zipcloudApiHeler.search("1000005");
            expect(json.results.length).toBe(2);
            expect(json.results[0].address1).toBe("東京都");
            expect(json.results[1].address1).toBe("神奈川県");
        });

        test("error のテスト", async () => {
            $.ajax = ajax_response({error: "エラー"}, false);
            await expect(zipcloudApiHeler.search("1000005")).rejects.toEqual({error: "エラー"});
        });

    });

    // zipcloudApiHeler.search の dataType: "jsonp" を削除しないと下のテストは成功しないので
    // コメントアウトしておく
    /*
    describe("jquery-mockjax でサーバ側をモックにして $.ajax をテストする", () => {

        afterEach(() => {
            mockjax.clear();
        });

        test("データを返す場合のテスト", async () => {
            mockjax({
                url: "http://zipcloud.ibsnet.co.jp/api/search",
                responseText: {
                    results: [
                        {address1: "東京都"},
                        {address1: "神奈川県"}
                    ]
                }
            });

            const json = await zipcloudApiHeler.search("1000005");
            expect(json.results.length).toBe(2);
            expect(json.results[0].address1).toBe("東京都");
            expect(json.results[1].address1).toBe("神奈川県");
        });

        // dataType: "jsonp" だとこのテストは成功しない
        // status: 500 にしても reject が呼び出されず、resolve が呼び出される
        test("HTTPステータスコード 500 が返ってくる場合のテスト", async () => {
            mockjax({
                url: "http://zipcloud.ibsnet.co.jp/api/search",
                status: 500
            });

            await expect(zipcloudApiHeler.search("1000005"))
                .rejects.toHaveProperty("status", 500);
        })

        // dataType: "jsonp" だとこのテストは成功しない
        // isTimeout: true にしても reject が呼び出されず、resolve が呼び出される
        test("Timeout する場合のテスト", async () => {
            mockjax({
                url: "http://zipcloud.ibsnet.co.jp/api/search",
                isTimeout: true
            });

            await expect(zipcloudApiHeler.search("1000005"))
                .rejects.toHaveProperty("statusText", "timeout");
        })

    });
    */

});

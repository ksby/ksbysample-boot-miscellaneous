/**
 * @jest-environment node
 */
"use strict";

const openWeatherMapHelper = require("lib/util/OpenWeatherMapHelper.js");
const nock = require('nock');

describe("ZipcloudApiHelper.js + Nock によるテスト", () => {

    beforeEach(() => {
        // jest.setTimeout のデフォルト値である５秒に戻す
        jest.setTimeout(5000);
        openWeatherMapHelper.init();
    });

    test("Nock で 200 + コンテンツを返すと then の方で処理される", async () => {
        nock("http://api.openweathermap.org")
            .get(/^\/data\/2\.5\/weather/)
            .reply(200, {
                weather: [
                    {
                        id: 500,
                        main: 'Rain',
                        description: 'light rain',
                        icon: '10d'
                    }
                ],
                name: 'Tokyo'
            });

        const response = await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo");
        const json = response.data;
        expect(json.name).toBe("Tokyo");
        expect(json.weather.length).toBe(1);
        expect(json.weather[0]).toHaveProperty("main", "Rain");
    });

    test("Nock で 500 を返すと catch の方で処理される", async done => {
        nock("http://api.openweathermap.org")
            .get(/^\/data\/2\.5\/weather/)
            .reply(500);

        await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo")
            .then(response => {
                done.fail("then に処理が来たらエラー");
            })
            .catch(e => {
                expect(e.response.status).toBe(500);
            });

        // test("...", async done => { ...}); と done を記述している場合には、テストの最後で done(); を呼び出さないと
        // ５秒待機した後、
        // Error: Timeout - Async callback was not invoked within timeout specified by jasmine.DEFAULT_TIMEOUT_INTERVAL.
        // のエラーメッセージを表示してテストが失敗する
        // ※https://facebook.github.io/jest/docs/en/asynchronous.html#callbacks 参照
        done();
    });

    test("Nock で 16秒で timeout させると catch の方で処理される", async done => {
        // openWeatherMapHelper の timeout の設定を変更せずに 15秒でテストする場合、
        // jest で async/await を使うと５秒くらいでタイムアウトさせられるので、30秒に延ばしておく
        jest.setTimeout(30000);

        nock("http://api.openweathermap.org")
            .get(/^\/data\/2\.5\/weather/)
            .delay(16000)
            .reply(200, {});

        await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo")
            .then(response => {
                done.fail("then に処理が来たらエラー");
            })
            .catch(e => {
                expect(e.message).toContain("timeout");
            });

        done();
    });

    test("Nock で 2秒で timeout させると catch の方で処理される", async done => {
        nock("http://api.openweathermap.org")
            .get(/^\/data\/2\.5\/weather/)
            .delay(2000)
            .reply(200, {});

        openWeatherMapHelper.setTimeout(1000);
        await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo")
            .then(response => {
                done.fail("then に処理が来たらエラー");
            })
            .catch(e => {
                expect(e.message).toContain("timeout");
            });

        done();
    });

});

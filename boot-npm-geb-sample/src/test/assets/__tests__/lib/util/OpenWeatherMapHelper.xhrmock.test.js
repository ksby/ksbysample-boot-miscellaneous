"use strict";

const openWeatherMapHelper = require("lib/util/OpenWeatherMapHelper.js");
const xhrmock = require('xhr-mock');

describe("ZipcloudApiHelper.js + xhr-mock によるテスト", () => {

    beforeEach(() => {
        // jest.setTimeout のデフォルト値である５秒に戻す
        jest.setTimeout(5000);
        xhrmock.setup();

        document.body.innerHTML = `
            <div>東京: <span id="weather"></span></div><br>
            <div id="error-msg"></div>
        `;
    });

    afterEach(() => {
        xhrmock.teardown();
    });

    test("xhr-mock で 200 + コンテンツを返すと then の方で処理される", async done => {
        xhrmock.get(/^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/
            , (req, res) => {
                return res
                    .status(200)
                    .body({
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
            });

        expect(document.getElementById("weather").textContent).toBe("");
        expect(document.getElementById("error-msg").textContent).toBe("");

        await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo")
            .then(response => {
                const json = response.data;
                expect(json.name).toBe("Tokyo");
                expect(json.weather.length).toBe(1);
                expect(json.weather[0]).toHaveProperty("main", "Rain");
                document.getElementById("weather").textContent = json.weather[0].main;
            })
            .catch(e => {
                done.fail("catch に処理が来たらエラー");
            });

        expect(document.getElementById("weather").textContent).toBe("Rain");
        expect(document.getElementById("error-msg").textContent).toBe("");

        done();
    });

    test("xhr-mock で 500 を返すと catch の方で処理される", async done => {
        xhrmock.get(/^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/
            , (req, res) => {
                return res
                    .status(500);
            });

        expect(document.getElementById("weather").textContent).toBe("");
        expect(document.getElementById("error-msg").textContent).toBe("");

        await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo")
            .then(response => {
                done.fail("then に処理が来たらエラー");
            })
            .catch(e => {
                expect(e.response.status).toBe(500);
                document.getElementById("error-msg").textContent = "エラー: HTTPステータスコード = " + e.response.status;
            });

        expect(document.getElementById("weather").textContent).toBe("");
        expect(document.getElementById("error-msg").textContent).toBe("エラー: HTTPステータスコード = 500");

        done();
    });

    test("xhr-mock で 2秒で timeout させると catch の方で処理される", async done => {
        xhrmock.get(/^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/
            , (req, res) => {
                return res
                    .timeout(true);
            });

        expect(document.getElementById("weather").textContent).toBe("");
        expect(document.getElementById("error-msg").textContent).toBe("");

        openWeatherMapHelper.setTimeout(1000);
        await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo")
            .then(response => {
                done.fail("then に処理が来たらエラー");
            })
            .catch(e => {
                expect(e.message).toContain("timeout");
                document.getElementById("error-msg").textContent = "エラー: timeout";
            });

        expect(document.getElementById("weather").textContent).toBe("");
        expect(document.getElementById("error-msg").textContent).toBe("エラー: timeout");

        done();
    });

});

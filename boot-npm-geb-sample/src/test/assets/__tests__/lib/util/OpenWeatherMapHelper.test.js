"use strict";

const openWeatherMapHelper = require("lib/util/OpenWeatherMapHelper.js");

describe("ZipcloudApiHelper.js のテスト", () => {

    test("OpenWeatherMap の API で東京の天気を取得する", async () => {
        const res = await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo");
        const json = res.data;
        expect(json.name).toBe("Tokyo");
        expect(json.weather[0]).toHaveProperty("main");
        expect(json.sys.country).toBe("JP");
    });

    test("then, catch を記述して then に処理がいくことを確認する", async () => {
        await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo")
            .then(response => {
                console.log(response);
            })
            .catch(e => {
                console.log(e);
            });
    });

    test("東京の天気を取得して div タグで囲んだ部分にセットする", async () => {
        document.body.innerHTML = `
            <div id="weather"></div>
        `;

        expect(document.getElementById("weather").textContent).toBe("");
        await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo")
            .then(response => {
                const json = response.data;
                document.getElementById("weather").textContent = json.weather[0].main;
            });
        expect(document.getElementById("weather").textContent).not.toBe("");
    });

});

"use strict";

const openWeatherMapHelper = require("lib/util/OpenWeatherMapHelper.js");
const xhrmock = require("xhr-mock");

describe("ZipcloudApiHelper.js + xhr-mock によるテスト", () => {
  beforeEach(() => {
    xhrmock.default.setup();
    document.body.innerHTML = `
            <div>東京: <span id="weather"></span></div><br>
            <div id="error-msg"></div>
        `;
  });

  afterEach(() => {
    xhrmock.default.teardown();
  });

  test("xhr-mock で 200 + コンテンツを返すと then の方で処理される", async () => {
    expect.assertions(7);
    xhrmock.default.get(
      /^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/,
      (req, res) => {
        return res.status(200).body({
          weather: [
            {
              id: 500,
              main: "Rain",
              description: "light rain",
              icon: "10d",
            },
          ],
          name: "Tokyo",
        });
      }
    );

    expect(document.getElementById("weather").textContent).toBe("");
    expect(document.getElementById("error-msg").textContent).toBe("");

    const response = await openWeatherMapHelper.getCurrentWeatherDataByCityName(
      "Tokyo"
    );
    const json = response.data;
    expect(json.name).toBe("Tokyo");
    expect(json.weather.length).toBe(1);
    expect(json.weather[0]).toHaveProperty("main", "Rain");
    document.getElementById("weather").textContent = json.weather[0].main;

    expect(document.getElementById("weather").textContent).toBe("Rain");
    expect(document.getElementById("error-msg").textContent).toBe("");
  });

  test("xhr-mock で 500 を返すと catch の方で処理される", async () => {
    expect.assertions(5);
    xhrmock.default.get(
      /^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/,
      (req, res) => {
        return res.status(500);
      }
    );

    expect(document.getElementById("weather").textContent).toBe("");
    expect(document.getElementById("error-msg").textContent).toBe("");

    try {
      await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo");
    } catch (e) {
      expect(e.response.status).toBe(500);
      document.getElementById("error-msg").textContent =
        "エラー: HTTPステータスコード = " + e.response.status;
    }

    expect(document.getElementById("weather").textContent).toBe("");
    expect(document.getElementById("error-msg").textContent).toBe(
      "エラー: HTTPステータスコード = 500"
    );
  });

  test("xhr-mock で 2秒で timeout させると catch の方で処理される", async () => {
    expect.assertions(5);
    xhrmock.default.get(
      /^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/,
      () => new Promise(() => {})
    );

    expect(document.getElementById("weather").textContent).toBe("");
    expect(document.getElementById("error-msg").textContent).toBe("");

    openWeatherMapHelper.setTimeout(1000);
    try {
      await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo");
    } catch (e) {
      expect(e.message).toContain("timeout");
      document.getElementById("error-msg").textContent = "エラー: timeout";
    }

    expect(document.getElementById("weather").textContent).toBe("");
    expect(document.getElementById("error-msg").textContent).toBe(
      "エラー: timeout"
    );
  });
});

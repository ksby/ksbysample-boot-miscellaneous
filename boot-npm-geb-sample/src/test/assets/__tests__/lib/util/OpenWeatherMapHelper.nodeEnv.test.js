/**
 * @jest-environment node
 */
"use strict";

const openWeatherMapHelper = require("lib/util/OpenWeatherMapHelper.js");
const nock = require("nock");

describe("ZipcloudApiHelper.js + Nock によるテスト", () => {
  beforeEach(() => {
    openWeatherMapHelper.init();
  });

  test("Nock で 200 + コンテンツを返すと then の方で処理される", async () => {
    expect.assertions(3);
    nock("http://api.openweathermap.org")
      .get(/^\/data\/2\.5\/weather/)
      .reply(200, {
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

    const response = await openWeatherMapHelper.getCurrentWeatherDataByCityName(
      "Tokyo"
    );
    const json = response.data;
    expect(json.name).toBe("Tokyo");
    expect(json.weather.length).toBe(1);
    expect(json.weather[0]).toHaveProperty("main", "Rain");
  });

  test("Nock で 500 を返すと catch の方で処理される", async () => {
    expect.assertions(1);
    nock("http://api.openweathermap.org")
      .get(/^\/data\/2\.5\/weather/)
      .reply(500);

    try {
      await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo");
    } catch (e) {
      expect(e.response.status).toBe(500);
    }
  });

  test("Nock で 16秒で timeout させると catch の方で処理される", async () => {
    expect.assertions(1);
    nock("http://api.openweathermap.org")
      .get(/^\/data\/2\.5\/weather/)
      .delay(16000)
      .reply(200, {});

    try {
      await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo");
    } catch (e) {
      expect(e.message).toContain("timeout");
    }
  });

  test("Nock で 2秒で timeout させると catch の方で処理される", async () => {
    expect.assertions(1);
    nock("http://api.openweathermap.org")
      .get(/^\/data\/2\.5\/weather/)
      .delay(2000)
      .reply(200, {});

    openWeatherMapHelper.setTimeout(1000);
    try {
      await openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo");
    } catch (e) {
      expect(e.message).toContain("timeout");
    }
  });
});

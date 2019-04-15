"use strict";

global.$ = require("jquery");
const mobx = require("mobx");
const mobxUtils = require("mobx-utils");
const openWeatherMapHelper = require("lib/util/OpenWeatherMapHelper.js");
const xhrmock = require("xhr-mock");
const computedAsyncMobx = require("computed-async-mobx");

describe("MobX の動作確認", () => {
  beforeEach(() => {
    xhrmock.default.setup();
  });

  afterEach(() => {
    xhrmock.default.teardown();
  });

  test("extendObservable でプロパティを１つ持つクラスを定義し、動作を確認する", () => {
    document.body.innerHTML = `
            <div id="value"></div>
        `;

    // mobx.extendObservable メソッドを使用して、Sample クラスを Observable として定義する
    //
    // function Sample() {...} で定義するなら以下のように記述する
    // function Sample() {
    //     mobx.extendObservable(this, {
    //         value: ""
    //     });
    // }
    //
    class Sample {
      constructor() {
        mobx.extendObservable(this, {
          value: ""
        });
      }
    }

    // sample.value が変更されたら $("#value").text() を更新するよう定義する
    const sample = new Sample();
    mobx.autorun(() => {
      $("#value").text(sample.value);
    });

    // sample.value の値を変更すると、
    // mobx.autorun で定義した関数が実行されて、
    // $("#value").text() に sample.value の値がセットされる
    expect($("#value").text()).toBe("");
    sample.value = "test";
    expect($("#value").text()).toBe("test");
  });

  test("extendObservable でプロパティを２つ持つクラスを定義し、動作を確認する", () => {
    document.body.innerHTML = `
            <div id="value"></div>
        `;

    class Sample {
      constructor() {
        mobx.extendObservable(this, {
          value: "",
          data: ""
        });
      }
    }

    const sample = new Sample();
    // mbox.autorun 内では sample.value しか参照していない
    mobx.autorun(() => {
      console.log(`★★★ mobx.autorun: sample.value = ${sample.value}`);
      $("#value").text(sample.value);
    });

    // sample.value を変更すると mbox.autorun で定義した関数が実行されるが、
    // sample.data を変更しても実行されない
    console.log('●●● sample.value = "PASS1"');
    sample.value = "PASS1";
    console.log('▲▲▲ sample.data = "1"');
    sample.data = "1";
    console.log('●●● sample.value = "PASS2"');
    sample.value = "PASS2";
  });

  test("autorun から computed で定義したプロパティを参照している場合の動作を確認する", () => {
    document.body.innerHTML = `
            <div id="personal">
                <input type="text" name="firstname" id="firstname" value="">
                <input type="text" name="lastname" id="lastname" value="">
                <div id="fullname"></div>
            </div>
        `;

    // mobx.computed() で定義した fullname プロパティから firstname + lastname の文字列
    // が取得できるようにする
    class Personal {
      constructor() {
        mobx.extendObservable(this, {
          firstname: "",
          lastname: "",
          fullname: mobx.computed(() => `${this.firstname}　${this.lastname}`)
        });
      }
    }

    const personal = new Personal();
    // mobx.autorun では personal.fullname を参照する
    mobx.autorun(() => {
      $("#fullname").text(personal.fullname);
    });

    // blur イベント発生時に View から入力された値を personal オブジェクトにセットする
    ["firstname", "lastname"].forEach(item => {
      $("#" + item).on("blur", event => {
        personal[item] = $(event.target).val();
      });
    });

    // $("#firstname").val() に "taro" と入力すると $("#fullname").text() も変更される
    $("#firstname")
      .val("taro")
      .blur();
    console.log("★★★ " + $("#fullname").text());
    // $("#lastname").val() に "tanaka" と入力しても $("#fullname").text() も変更される
    $("#lastname")
      .val("tanaka")
      .blur();
    console.log("★★★ " + $("#fullname").text());
  });

  test("autorun からメソッドを参照している場合の動作を確認する", () => {
    document.body.innerHTML = `
            <div id="personal">
                <input type="text" name="firstname" id="firstname" value="">
                <input type="text" name="lastname" id="lastname" value="">
                <div id="fullname"></div>
            </div>
        `;

    // fullname メソッドから firstname + lastname の文字列
    // が取得できるようにする
    class Personal {
      constructor() {
        mobx.extendObservable(this, {
          firstname: "",
          lastname: "",
          fullname() {
            return `${this.firstname}　${this.lastname}`;
          }
        });
      }
    }

    const personal = new Personal();
    // mobx.autorun では personal.fullname() を参照する
    mobx.autorun(() => {
      $("#fullname").text(personal.fullname());
    });

    // blur イベント発生時に View から入力された値を personal オブジェクトにセットする
    ["firstname", "lastname"].forEach(item => {
      $("#" + item).on("blur", event => {
        personal[item] = $(event.target).val();
      });
    });

    // $("#firstname").val() に "taro" と入力すると $("#fullname").text() も変更される
    $("#firstname")
      .val("taro")
      .blur();
    console.log("★★★ " + $("#fullname").text());
    // $("#lastname").val() に "tanaka" と入力しても $("#fullname").text() も変更される
    $("#lastname")
      .val("tanaka")
      .blur();
    console.log("★★★ " + $("#fullname").text());
  });

  test("autorun はプロパティの値を変更した時だけでなく、最初の定義時にも関数を実行する", () => {
    class Sample {
      constructor() {
        mobx.extendObservable(this, {
          value: ""
        });
      }
    }

    let cnt = 1;
    const sample = new Sample();
    console.log("(1) mobx.autorun は定義時に１度実行される");
    mobx.autorun(() => {
      console.log(`${cnt}回目: mobx.autorun の関数が実行されました`);
      $("#value").text(sample.value);
      cnt++;
    });

    console.log("(2) mobx.autorun はプロパティを変更しても実行される");
    sample.value = "1";
  });

  test("autorun の初回実行時にプロパティを参照しないと、プロパティを変更しても関数は実行されない", () => {
    class Sample {
      constructor() {
        this.autorunFirstFlg = true;
        mobx.extendObservable(this, {
          value: ""
        });
      }
    }

    let cnt = 1;
    const sample = new Sample();
    console.log(
      "(1) mobx.autorun の最初の定義時にプロパティを参照しないようにする"
    );
    mobx.autorun(() => {
      console.log(`${cnt}回目: mobx.autorun の関数が実行されました`);
      if (sample.autorunFirstFlg) {
        sample.autorunFirstFlg = false;
        return;
      }

      // 最初の実行時にはここを通らない
      $("#value").text(sample.value);
      cnt++;
    });

    console.log("(2) プロパティを変更しても mobx.autorun は実行されない");
    sample.value = "1";
  });

  test("when は条件を満たした時に１度だけ動作する", () => {
    document.body.innerHTML = `
            <div id="value"></div>
        `;

    class Sample {
      constructor() {
        mobx.extendObservable(this, {
          value: "",
          whenExecuteFlg: false
        });
      }
    }

    let cnt = 1;
    const sample = new Sample();
    console.log("mobx.when は最初の定義時には実行されない");
    mobx.when(
      // 第１引数の関数の条件を満たした時だけ、第２引数の関数が実行される
      // また第１引数の条件で参照する変数は observable でなければいけない
      () => sample.whenExecuteFlg === true,
      () => {
        console.log(`${cnt}回目: mobx.when の関数が実行されました`);
        $("#value").text(sample.value);
        cnt++;
      }
    );

    // sample.value の値を変更するたけでは $("#value").text() には反映されない
    sample.value = "test";
    expect($("#value").text()).toBe("");
    // sample.whenExecuteFlg を true にすると反映される
    sample.whenExecuteFlg = true;
    expect($("#value").text()).toBe("test");
    // sample.whenExecuteFlg を false に戻して再度 true にしても、もう mobx.when は実行されない
    sample.whenExecuteFlg = false;
    sample.value = "sample";
    sample.whenExecuteFlg = true;
    expect($("#value").text()).toBe("test");
  });

  test("when + mobx-utils.fromPromise のサンプル", done => {
    // xhr-mock でモックを定義する
    xhrmock.default.get(
      /^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/,
      (req, res) => {
        return res.status(200).body({
          weather: [
            {
              id: 500,
              main: "Rain",
              description: "light rain",
              icon: "10d"
            }
          ],
          name: "Tokyo"
        });
      }
    );

    document.body.innerHTML = `
            <div id="weather"></div>
        `;

    // mobx-utils の fromPromise メソッドを使用して
    // openWeatherMapHelper.getCurrentWeatherDataByCityName を呼び出す
    const result = mobxUtils.fromPromise(
      openWeatherMapHelper.getCurrentWeatherDataByCityName("Tokyo")
    );
    // 非同期処理が完了したら ( result.state が "pending" でなくなったら )、
    // 取得した天気を $("#weather").text() にセットする
    mobx.when(
      () => result.state !== "pending",
      () => {
        const json = result.value.data;
        $("#weather").text(json.weather[0].main);
      }
    );

    // 1秒後に $("#weather").text() にモックで定義した天気("Rain")がセットされていることを確認する
    expect($("#wheather").text()).toBe("");
    setTimeout(() => {
      expect($("#weather").text()).toBe("Rain");
      done();
    }, 1000);
  });

  test("autorunAsync は observable なプロパティが変更された時に動作する（定義時は動作しない）", done => {
    // xhr-mock でモックを定義する
    xhrmock.default.get(
      /^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/,
      (req, res) => {
        return res.status(200).body({
          weather: [
            {
              id: 500,
              main: "Rain",
              description: "light rain",
              icon: "10d"
            }
          ],
          name: "Tokyo"
        });
      }
    );

    document.body.innerHTML = `
            <div id="area">
                <input type="text" name="name" id="name" value="">
                <div id="weather"></div>
            </div>
        `;

    class Area {
      constructor() {
        mobx.extendObservable(this, {
          name: "",
          weather: ""
        });
      }
    }

    const area = new Area();
    // area.name が更新されたら OpenWeatherMap の API で天気を取得し、area.weather にセットする
    mobx.autorunAsync(async () => {
      console.log(
        "(3) area.name が更新されたら OpenWeatherMap の API で天気を取得し、area.weather にセットする"
      );
      const res = await openWeatherMapHelper.getCurrentWeatherDataByCityName(
        area.name
      );
      const json = res.data;
      area.weather = json.weather[0].main;
    });
    // area.weather が更新されたら $("#weather").text() にセットする
    mobx.autorun(() => {
      console.log(
        '(4) area.weather が更新されたら $("#weather").text() にセットする'
      );
      $("#weather").text(area.weather);
    });

    // $("#name").val() に入力された値を area.name にセットする
    $("#name").on("blur", event => {
      console.log(
        '(2) $("#name").val() に入力された値を area.name にセットする'
      );
      area.name = $(event.target).val();
    });

    console.log('(1) $("#name").val() に Tokyo と入力する');
    $("#name")
      .val("Tokyo")
      .blur();

    setTimeout(() => {
      console.log(
        '(5) $("#weather").text() に Rain がセットされていることを確認する'
      );
      expect($("#weather").text()).toBe("Rain");
      // なぜかここに done(); を書くと console.log("(5) ..."); のログが出力されない
      // done();
    }, 1000);

    setTimeout(() => {
      done();
    }, 1100);
  });

  test("autorun を複数定義すると最後に定義したもの→最初に定義したものの順に実行される", () => {
    document.body.innerHTML = `
            <div id="value"></div>
            <div id="data"></div>
            <div id="all"></div>
        `;

    class Sample {
      constructor() {
        mobx.extendObservable(this, {
          value: "",
          data: "",
          all: mobx.computed(() => `${this.value}, ${this.data}`)
        });
      }
    }

    const sample = new Sample();
    mobx.autorun(() => {
      console.log("PASS1");
      $("#value").text(sample.value);
    });
    mobx.autorun(() => {
      console.log("PASS2");
      $("#data").text(sample.data);
    });
    mobx.autorun(() => {
      console.log("PASS3");
      $("#all").text(sample.all);
    });

    expect($("#value").text()).toBe("");
    expect($("#data").text()).toBe("");
    expect($("#all").text()).toBe(", ");

    console.log("★★★");
    sample.value = "1";
    expect($("#value").text()).toBe("1");
    expect($("#data").text()).toBe("");
    expect($("#all").text()).toBe("1, ");

    console.log("●●●");
    sample.data = "a";
    expect($("#value").text()).toBe("1");
    expect($("#data").text()).toBe("a");
    expect($("#all").text()).toBe("1, a");
  });

  test("computed-async-mobx を使用するとクラス内に API 呼び出しの非同期処理を定義できる", done => {
    // xhr-mock でモックを定義する
    xhrmock.default.get(
      /^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/,
      (req, res) => {
        return res.status(200).body({
          weather: [
            {
              id: 500,
              main: "Rain",
              description: "light rain",
              icon: "10d"
            }
          ],
          name: "Tokyo"
        });
      }
    );

    document.body.innerHTML = `
            <div id="area">
                <input type="text" name="name" id="name" value="">
                <div id="weather"></div>
            </div>
        `;

    // API を呼び出す処理を mobx.autorunAsync(...) ではなく、
    // computed-async-mobx.asyncComputed(...) を使用してクラス内に定義する
    class Area {
      constructor() {
        mobx.extendObservable(this, {
          name: "",
          weather: computedAsyncMobx.asyncComputed("", 0, async () => {
            console.log("PASS1-1");
            if (this.name === "") {
              return "";
            }
            console.log("PASS1-2");
            console.log(
              "(3) area.name が更新されたら OpenWeatherMap の API で天気を取得し、area.weather にセットする"
            );
            const res = await openWeatherMapHelper.getCurrentWeatherDataByCityName(
              this.name
            );
            const json = res.data;
            return json.weather[0].main;
          })
        });
      }
    }

    const area = new Area();
    // area.weather が更新されたら $("#weather").text() にセットする
    mobx.autorun(() => {
      // area.weather ではなく area.weather.get() で値を取得する点に注意する
      console.log(
        '(4) area.weather が更新されたら $("#weather").text() にセットする'
      );
      $("#weather").text(area.weather.get());
    });

    // $("#name").val() に入力された値を area.name にセットする
    $("#name").on("blur", event => {
      console.log(
        '(2) $("#name").val() に入力された値を area.name にセットする'
      );
      area.name = $(event.target).val();
    });

    console.log('(1) $("#name").val() に Tokyo と入力する');
    $("#name")
      .val("Tokyo")
      .blur();

    let doneFlg = false;
    setTimeout(() => {
      console.log(
        '(5) $("#weather").text() に Rain がセットされていることを確認する'
      );
      expect($("#weather").text()).toBe("Rain");
      // なぜかここに done(); を書くと console.log("(5) ..."); のログが出力されない
      // done();
      doneFlg = true;
    }, 1000);

    setTimeout(() => {
      if (doneFlg) {
        done();
      }
    }, 1100);
  });

  test("mobx.action を使用して API 呼び出しの非同期処理を記述する", done => {
    // xhr-mock でモックを定義する
    xhrmock.default.get(
      /^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/,
      (req, res) => {
        return res.status(200).body({
          weather: [
            {
              id: 500,
              main: "Rain",
              description: "light rain",
              icon: "10d"
            }
          ],
          name: "Tokyo"
        });
      }
    );

    document.body.innerHTML = `
            <div id="area">
                <input type="text" name="name" id="name" value="">
                <div id="weather"></div>
            </div>
        `;

    // API を呼び出す処理を mobx.autorunAsync(...) ではなく、
    // mobx.action(...) を使用してクラス内に定義する
    class Area {
      constructor() {
        mobx.extendObservable(this, {
          name: "",
          weather: "",
          getWeather: mobx.action(async () => {
            console.log(
              "(3) area.name が更新されたら OpenWeatherMap の API で天気を取得し、area.weather にセットする"
            );
            const res = await openWeatherMapHelper.getCurrentWeatherDataByCityName(
              this.name
            );
            const json = res.data;
            this.weather = json.weather[0].main;
          })
        });
      }
    }

    const area = new Area();
    // area.weather が更新されたら $("#weather").text() にセットする
    mobx.autorun(() => {
      console.log(
        '(4) area.weather が更新されたら $("#weather").text() にセットする'
      );
      $("#weather").text(area.weather);
    });

    // $("#name").val() に入力された値を area.name にセットする
    $("#name").on("blur", event => {
      console.log(
        '(2) $("#name").val() に入力された値を area.name にセットする'
      );
      area.name = $(event.target).val();
      // action は自動実行はされないので、ここで area.getWeather(); を呼び出す
      area.getWeather();
    });

    console.log('(1) $("#name").val() に Tokyo と入力する');
    $("#name")
      .val("Tokyo")
      .blur();

    let doneFlg = false;
    setTimeout(() => {
      console.log(
        '(5) $("#weather").text() に Rain がセットされていることを確認する'
      );
      expect($("#weather").text()).toBe("Rain");
      // なぜかここに done(); を書くと console.log("(5) ..."); のログが出力されない
      // done();
      doneFlg = true;
    }, 1000);

    setTimeout(() => {
      if (doneFlg) {
        done();
      }
    }, 1100);
  });

  test("mobx.reaction を使用すれば、プロパティ更新時に自動で非同期処理を実行できる", done => {
    // xhr-mock でモックを定義する
    xhrmock.default.get(
      /^http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather/,
      (req, res) => {
        return res.status(200).body({
          weather: [
            {
              id: 500,
              main: "Rain",
              description: "light rain",
              icon: "10d"
            }
          ],
          name: "Tokyo"
        });
      }
    );

    document.body.innerHTML = `
            <div id="area">
                <input type="text" name="name" id="name" value="">
                <div id="weather"></div>
            </div>
        `;

    // API を呼び出す処理を mobx.autorunAsync(...) ではなく、
    // mobx.reaction(...) を使用してクラス内に定義する
    class Area {
      constructor() {
        mobx.extendObservable(this, {
          name: "",
          weather: ""
        });
        mobx.reaction(
          () => this.name,
          async () => {
            console.log(
              "(3) area.name が更新されたら OpenWeatherMap の API で天気を取得し、area.weather にセットする"
            );
            const res = await openWeatherMapHelper.getCurrentWeatherDataByCityName(
              this.name
            );
            const json = res.data;
            this.weather = json.weather[0].main;
          }
        );
      }
    }

    const area = new Area();
    // area.weather が更新されたら $("#weather").text() にセットする
    mobx.autorun(() => {
      console.log(
        '(4) area.weather が更新されたら $("#weather").text() にセットする'
      );
      $("#weather").text(area.weather);
    });

    // $("#name").val() に入力された値を area.name にセットする
    $("#name").on("blur", event => {
      console.log(
        '(2) $("#name").val() に入力された値を area.name にセットする'
      );
      area.name = $(event.target).val();
    });

    console.log('(1) $("#name").val() に Tokyo と入力する');
    $("#name")
      .val("Tokyo")
      .blur();

    let doneFlg = false;
    setTimeout(() => {
      console.log(
        '(5) $("#weather").text() に Rain がセットされていることを確認する'
      );
      expect($("#weather").text()).toBe("Rain");
      // なぜかここに done(); を書くと console.log("(5) ..."); のログが出力されない
      // done();
      doneFlg = true;
    }, 1000);

    setTimeout(() => {
      if (doneFlg) {
        done();
      }
    }, 1100);
  });

  test("mobx.reaction で複数プロパティを監視するには [] で囲んで指定する", () => {
    document.body.innerHTML = `
            <div id="sample"></div>
        `;

    class Sample {
      constructor() {
        mobx.extendObservable(this, {
          value: "",
          data: "",
          pass: ""
        });
        mobx.reaction(
          () => [this.value, this.data, this.pass],
          ([value, data, pass]) => {
            $("#sample").text(
              `value = ${value}, data = ${data}, pass = ${pass}`
            );
          }
        );
      }
    }

    const sample = new Sample();
    console.log("★★★ " + $("#sample").text());
    sample.value = "a";
    console.log("★★★ " + $("#sample").text());
    sample.data = "1";
    console.log("★★★ " + $("#sample").text());
    sample.pass = "PASS1";
    console.log("★★★ " + $("#sample").text());
    sample.value = "b";
    console.log("★★★ " + $("#sample").text());
    sample.data = "2";
    console.log("★★★ " + $("#sample").text());
    sample.pass = "PASS2";
    console.log("★★★ " + $("#sample").text());
  });
});

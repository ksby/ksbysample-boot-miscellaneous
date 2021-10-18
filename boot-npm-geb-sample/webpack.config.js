const webpack = require("webpack");
const TerserPlugin = require("terser-webpack-plugin");

// --mode オプションで指定された文字列を参照したい場合には argv.mode を参照する
module.exports = (env, argv) => {
  return {
    entry: {
      "js/inquiry/input01": ["./src/main/assets/js/inquiry/input01.js"],
      "js/inquiry/input02": ["./src/main/assets/js/inquiry/input02.js"],
      "js/inquiry/input03": ["./src/main/assets/js/inquiry/input03.js"],
      "js/inquiry/confirm": ["./src/main/assets/js/inquiry/confirm.js"],
    },
    output: {
      path: __dirname + "/src/main/resources/static",
      publicPath: "/",
      filename: "[name].js",
    },
    cache:
      argv.mode === "production"
        ? false
        : {
            type: "filesystem",
            buildDependencies: {
              config: [__filename],
            },
          },
    resolve: {
      modules: ["node_modules", "src/main/assets/js"],
      alias: {
        jquery: "jquery",
      },
    },
    module: {
      rules: [
        {
          test: /\.js$/,
          exclude: [/node_modules/, /jquery.autoKana.js$/],
          loader: "eslint-loader",
        },
      ],
    },
    optimization: {
      minimizer: [
        new TerserPlugin({
          terserOptions: {
            ecma: 5,
            output: {
              comments: false,
            },
            compress: {
              dead_code: true,
              drop_console: true,
            },
          },
        }),
      ],
    },
    plugins: [
      new webpack.ProvidePlugin({
        $: "jquery",
        jQuery: "jquery",
      }),
    ],
    devtool: argv.mode === "production" ? false : "inline-source-map",
  };
};

const webpack = require("webpack");
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

// --mode オプションで指定された文字列を参照したい場合には argv.mode を参照する
module.exports = (env, argv) => {
    return {
        entry: {
            "js/inquiry/input01": ["./src/main/assets/js/inquiry/input01.js"],
            "js/inquiry/input02": ["./src/main/assets/js/inquiry/input02.js"],
            "js/inquiry/input03": ["./src/main/assets/js/inquiry/input03.js"],
            "js/inquiry/confirm": ["./src/main/assets/js/inquiry/confirm.js"]
        },
        output: {
            path: __dirname + "/src/main/resources/static",
            publicPath: "/",
            filename: "[name].js"
        },
        resolve: {
            modules: [
                "node_modules",
                "src/main/assets/js"
            ],
            alias: {
                jquery: "jquery"
            }
        },
        module: {
            rules: [
                {
                    test: /\.js$/,
                    exclude: [
                        /node_modules/,
                        /jquery.autoKana.js$/
                    ],
                    loader: "eslint-loader"
                }
            ]
        },
        optimization: {
            minimizer: [
                new UglifyJsPlugin({
                    uglifyOptions: {
                        compress: true,
                        ecma: 5,
                        output: {
                            comments: false
                        },
                        compress: {
                            dead_code: true,
                            drop_console: true
                        }
                    },
                    sourceMap: false
                })
            ]
        },
        plugins: [
            new webpack.ProvidePlugin({
                $: "jquery",
                jQuery: "jquery"
            })
        ],
        devtool: "inline-source-map"
    };
};

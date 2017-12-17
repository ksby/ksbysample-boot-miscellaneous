var webpack = require('webpack');

module.exports = {
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
    plugins: [
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery"
        })
    ],
    devtool: "inline-source-map"
};

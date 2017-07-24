module.exports = {
    entry: {
        "js/app": ["./src/main/assets/js/app.js"]
    },
    output: {
        path: __dirname + "/src/main/resources/static",
        publicPath: "/",
        filename: "[name].js"
    }
};

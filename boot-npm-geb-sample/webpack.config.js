module.exports = {
    entry: {
        "js/app": ["./src/main/assets/js/app.js"],
        "js/inquiry/input01": ["./src/main/assets/js/inquiry/input01.js"],
        "js/inquiry/input02": ["./src/main/assets/js/inquiry/input02.js"],
        "js/inquiry/input03": ["./src/main/assets/js/inquiry/input03.js"]
    },
    output: {
        path: __dirname + "/src/main/resources/static",
        publicPath: "/",
        filename: "[name].js"
    }
};

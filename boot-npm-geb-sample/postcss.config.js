module.exports = {
  plugins: [
    require("stylelint"),
    require("autoprefixer")({
      browserlist: ["last 2 versions"]
    }),
    require("cssnano")({
      preset: "default"
    })
  ]
};

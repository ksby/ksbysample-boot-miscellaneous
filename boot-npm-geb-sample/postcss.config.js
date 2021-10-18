module.exports = {
  plugins: [
    require("stylelint"),
    require("autoprefixer"),
    require("cssnano")({
      preset: "default",
    }),
  ],
};

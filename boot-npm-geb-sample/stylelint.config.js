"use strict";

module.exports = {
  extends: "stylelint-config-standard",
  rules: {
    "comment-empty-line-before": [
      "always",
      {
        ignore: ["after-comment"],
      },
    ],
    indentation: 4,
    "number-leading-zero": "never",
  },
};

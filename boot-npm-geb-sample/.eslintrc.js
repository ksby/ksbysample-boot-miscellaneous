module.exports = {
  extends: ["airbnb-base/legacy", "plugin:prettier/recommended"],
  env: {
    jquery: true
  },
  rules: {
    // requires to declare all vars on top of their containing scope
    "vars-on-top": "off",

    // require function expressions to have a name
    // http://eslint.org/docs/rules/func-names
    "func-names": "off",

    // disallow mixed 'LF' and 'CRLF' as linebreaks
    // http://eslint.org/docs/rules/linebreak-style
    "linebreak-style": ["error", "windows"],

    // specify whether double or single quotes should be used
    quotes: ["error", "double", {avoidEscape: true}],

    // disallow use of variables before they are defined
    "no-use-before-define": [
      "error",
      {functions: false, classes: true, variables: true}
    ]
  }
};

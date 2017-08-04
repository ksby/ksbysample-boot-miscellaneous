module.exports = {
    extends: "stylelint-config-standard",
    rules: {
        "at-rule-empty-line-before": [
            "always",
            {
                "except": ["after-same-name"],
                "ignore": ["after-comment"]
            }
        ],
        "comment-empty-line-before": [
            "always",
            {
                "ignore": ["after-comment"]
            }
        ],
        "indentation": 4,
        "number-leading-zero": "never",
        "rule-empty-line-before": "never-multi-line"
    }
};

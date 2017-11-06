module.exports = {
    "extends": "airbnb-base/legacy",
    "env": {
        "jquery": true
    },
    "rules": {
        // requires to declare all vars on top of their containing scope
        'vars-on-top': 'off',

        // require function expressions to have a name
        // http://eslint.org/docs/rules/func-names
        'func-names': 'off',

        // enforce consistent line breaks inside function parentheses
        // https://eslint.org/docs/rules/function-paren-newline
        'function-paren-newline': 'off',

        // this option sets a specific tab width for your code
        // http://eslint.org/docs/rules/indent
        indent: ['error', 4, {
            SwitchCase: 1,
            VariableDeclarator: 1,
            outerIIFEBody: 1,
            // MemberExpression: null,
            FunctionDeclaration: {
                parameters: 1,
                body: 1
            },
            FunctionExpression: {
                parameters: 1,
                body: 1
            },
            CallExpression: {
                arguments: 1
            },
            ArrayExpression: 1,
            ObjectExpression: 1,
            ImportDeclaration: 1,
            flatTernaryExpressions: false,
            ignoredNodes: ['JSXElement', 'JSXElement *']
        }],

        // disallow mixed 'LF' and 'CRLF' as linebreaks
        // http://eslint.org/docs/rules/linebreak-style
        'linebreak-style': ['error', 'windows'],

        // specify whether double or single quotes should be used
        quotes: ['error', 'double', { avoidEscape: true }],

        // require padding inside curly braces
        'object-curly-spacing': ['error', 'never'],

        // disallow use of variables before they are defined
        'no-use-before-define': ['error', { functions: false, classes: true, variables: true }],

        // specify the maximum length of a line in your program
        // http://eslint.org/docs/rules/max-len
        'max-len': ['error', 120, 2, {
            ignoreUrls: true,
            ignoreComments: false,
            ignoreRegExpLiterals: true,
            ignoreStrings: true,
            ignoreTemplateLiterals: true
        }]
    }
};

module.exports = {
    plugins: [
        require('stylelint'),
        require('autoprefixer')({
            browsers: [
                "last 2 versions"
            ]
        }),
        require('cssnano')({
            preset: 'default'
        })
    ]
};

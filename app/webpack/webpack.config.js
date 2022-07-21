const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const sveltePreprocess = require('svelte-preprocess');
const sass = require('svelte-preprocess-sass');

module.exports = {
    mode: "development",

    entry: {
        'main': './app/typescript/Loader.ts',
    },

    output: {
        path: path.resolve('./public/bundles'),
        publicPath: '/assets/',
        filename: "[name].js",
    },

    resolve: {
        extensions: ['.ts', '.js', '.scss', '.svelte'],
    },

    module: {
        rules: [
            {
                test: /\.(svelte)$/,
                exclude: /node_modules/,
                use: {
                    loader: 'svelte-loader',
                    options: {
                        emitCss: true,
                        preprocess: sveltePreprocess({
                            style: sass.sass({
                                },
                                {
                                    name: 'scss',
                                }),
                            scss: {
                              // prependData: `@import './app/com/informaticon/svelte/example/stylesheets/var.scss';`,
                            },
                            typescript: {
                                // compilerOptions: {
                                //     "module": "ES6",
                                //     "moduleResolution": "classic"
                                // }
                            }
                        }),
                    },
                },
            },
            {
                // required to prevent errors from Svelte on Webpack 5+, omit on Webpack 4
                test: /node_modules\/svelte\/.*\.mjs$/,
                resolve: {
                    fullySpecified: false
                }
            },
            {
                test: /\.(sa|sc)ss$/,
                exclude: /node_modules/,
                use: [
                    'raw-loader',
                    {
                        loader: MiniCssExtractPlugin.loader,
                    },
                    'css-loader',

                    {
                        loader: "sass-loader",
                    }
                ]
            },
            {
                test: /\.css$/,
                exclude: /node_modules/,
                use: [
                    'raw-loader',
                    {
                        loader: MiniCssExtractPlugin.loader,
                    },
                    { loader: 'css-loader'},
                ]
            },
            {
                test: /\.ts$/,
                use: [{
                    loader: 'ts-loader',

                }]
            },
        ]
    },

    plugins: [
        new MiniCssExtractPlugin({
            filename: "main.css"
        })
    ],

};
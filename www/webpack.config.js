const path = require('path');
const CopyPlugin = require('copy-webpack-plugin');

module.exports = {
    entry: './src/core.ts',
    mode: 'development',
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'main.js'
    },
    resolve: {
        extensions: ['.ts', '.tsx', '.js', '.json']
    },
    module: {
        rules: [
            {
                test: /\.less$/,
                use: [
                    {
                        loader: 'css-loader'
                    },
                    {
                        loader: 'less-loader'
                    }
                ]
            },
            {
                test: /\.(ts|js)x?$/,
                exclude: /node_modules/,
                loader: 'babel-loader',
            },
            {
                test: /\.css$/i,
                use: ['to-string-loader', 'css-loader'],
            }
        ]
    },
    devtool: 'source-map',
    plugins: [
        new CopyPlugin({
            patterns: [
                {from: './node_modules/echarts/dist/echarts.js', to: '.'},
                {from: './node_modules/echarts/dist/echarts.js.map', to: '.'}
            ],
        }),
    ]
};

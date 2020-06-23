var path = require('path');
const CopyPlugin = require('copy-webpack-plugin');

module.exports = {
    entry: './src/core.ts',
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'main.js'
    },
    resolve: {
        extensions: ['.ts', '.tsx', '.js', '.json']
    },
    module: {
        rules: [{
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
    plugins: [
        new CopyPlugin({
            patterns: [
                {from: './node_modules/echarts/dist/echarts.js', to: '.'}
            ],
        }),
    ]
};

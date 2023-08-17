const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { ProvidePlugin, EnvironmentPlugin } = require('webpack');
const dotenv = require('dotenv');

// Load environment variables from '.env' file
const env = dotenv.config().parsed || {};

module.exports = {
  entry: {
    main: './src/index.tsx',
  },
  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, 'dist'),
    clean: true,
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, 'public/index.html'),
    }),
    new ProvidePlugin({
      React: 'react',
      process: 'process/browser.js',
    }),
    new EnvironmentPlugin({
      REACT_APP_API_DEFAULT_DEV: env.REACT_APP_API_DEFAULT_DEV,
      REACT_APP_API_DEFAULT_PROD: env.REACT_APP_API_DEFAULT_PROD,
    }),
  ],
  resolve: {
    extensions: ['.tsx', '.ts', '.jsx', '.js'],
  },
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env', '@babel/preset-react'],
            plugins: ['babel-plugin-styled-components'],
          },
        },
      },
      {
        test: /\.(png|jpe?g)$/,
        type: 'asset',
      },
      {
        test: /\.svg$/,
        use: ['@svgr/webpack'],
      },
    ],
  },
};

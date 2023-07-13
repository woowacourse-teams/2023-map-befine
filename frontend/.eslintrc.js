module.exports = {
  extends: ['airbnb', 'prettier', 'plugin:storybook/recommended'],
  plugins: ['simple-import-sort'],
  rules: {
    'import/extensions': ['off'],
    'react/react-in-jsx-scope': 'off',
    'react/jsx-filename-extension': ['error', {
      extensions: ['.js', '.jsx', '.ts', '.tsx']
    }],
    'import/no-extraneous-dependencies': ['error', {
      devDependencies: true
    }],
    'simple-import-sort/imports': 'error',
    'simple-import-sort/exports': 'error'
  }
};
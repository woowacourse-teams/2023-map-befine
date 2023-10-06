module.exports = {
  extends: ['airbnb', 'prettier', 'plugin:storybook/recommended'],
  plugins: ['simple-import-sort'],
  parser: '@typescript-eslint/parser',
  rules: {
    'import/extensions': ['off'],
    'react/react-in-jsx-scope': 'off',
    'react/jsx-filename-extension': [
      'error',
      {
        extensions: ['.js', '.jsx', '.ts', '.tsx'],
      },
    ],
    'import/no-extraneous-dependencies': [
      'error',
      {
        devDependencies: true,
      },
    ],
    'simple-import-sort/imports': 'error',
    'simple-import-sort/exports': 'error',
    'no-undef': 'off',
    'no-unused-vars': 'off',
    '@typescript-eslint/no-unused-vars': 'off',
    'import/prefer-default-export': 'off',
    'no-use-before-define': 'off',
    'react/require-default-props': 'off',
  },
  settings: {
    'import/resolver': {
      node: {
        extensions: ['.js', '.jsx', '.ts', '.tsx'],
      },
    },
  },
};

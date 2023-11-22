import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  html,
  body,
  h2,
  h3,
  textarea{
    padding:0;
    margin:0;
    font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol';
  }
  
  a {
    text-decoration:none;
    color: #000;
    transition: all 0.3s ease-in-out;
  }

  ul {
    padding-left: 0;
    margin:0;
    list-style-type: none;
  }

  * {
    box-sizing:border-box;
  }

  input[type='number']::-webkit-outer-spin-button,
  input[type='number']::-webkit-inner-spin-button {
    appearance: none;
    margin: 0;
  }
  `;

export default GlobalStyle;

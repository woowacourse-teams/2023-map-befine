import type { Preview } from '@storybook/react';
import { initialize, mswDecorator } from 'msw-storybook-addon';

import { handlers } from '../src/mocks/handlers';
import React from 'react';
import { ThemeProvider } from 'styled-components';
import theme from '../src/themes';
import GlobalStyle from '../src/GlobalStyle';
import { MemoryRouter } from 'react-router-dom';
import { worker } from '../src/mocks/browser';

initialize();

const preview: Preview = {
  parameters: {
    msw: handlers,
    actions: { argTypesRegex: '^on[A-Z].*' },
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/,
      },
    },
  },
  decorators: [
    mswDecorator,
    (Story) => (
      <MemoryRouter initialEntries={['/']}>
        <ThemeProvider theme={theme}>
          <GlobalStyle />
          <Story />
        </ThemeProvider>
      </MemoryRouter>
    ),
  ],
};

if (typeof global.process === 'undefined') {
  worker.start();
}

export default preview;

import { css } from 'styled-components';

export const setFullScreenResponsive = () => {
  return css`
    @media (max-width: 1076px) {
      width: 684px;
    }

    @media (max-width: 724px) {
      width: 332px;
    }
  `;
};

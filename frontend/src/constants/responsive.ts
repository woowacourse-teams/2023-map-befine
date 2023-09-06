import { css } from 'styled-components';

export const fullScreenResponsive = () => {
  return css`
    @media (max-width: 1076px) {
      width: 684px;
    }

    @media (max-width: 724px) {
      width: 332px;
    }

    @media (max-width: 372px) {
      width: 332px;
    }
  `;
};

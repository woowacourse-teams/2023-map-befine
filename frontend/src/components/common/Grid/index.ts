import { CSSProperties } from 'react';
import styled, { css } from 'styled-components';

import Box, { BoxProps } from '../Box';

interface GridProps extends BoxProps {
  rows: CSSProperties['gridRow'];
  columns: CSSProperties['gridColumn'];
  gap?: CSSProperties['gap'];
  $mediaQueries?: [number, number | string][];
}

const convertColumnsWithMediaQuery = (
  mediaQueries: [number, number | string][],
) =>
  mediaQueries.map(
    ([condition, value]) => css`
      @media (max-width: ${condition}px) {
        grid-template-columns: ${typeof value === 'number'
          ? `repeat(${value}, minmax(auto, 1fr))`
          : value};
      }
    `,
  );

const Grid = styled(Box)<GridProps>`
  display: grid;
  grid-template-rows: ${({ rows }) =>
    typeof rows === 'number' ? `repeat(${rows}, minmax(auto, 1fr))` : rows};
  grid-template-columns: ${({ columns }) =>
    typeof columns === 'number'
      ? `repeat(${columns}, minmax(auto, 1fr))`
      : columns};
  gap: ${({ gap }) => (typeof gap === 'number' ? `${gap}px` : gap)};

  ${({ $mediaQueries }) =>
    $mediaQueries && convertColumnsWithMediaQuery($mediaQueries)}
`;

export default Grid;

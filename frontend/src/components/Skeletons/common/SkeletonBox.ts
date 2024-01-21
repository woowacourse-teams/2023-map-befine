import styled, { keyframes } from 'styled-components';

import { convertCSS } from '../../../utils/convertCSS';

interface Props {
  width?: number | string;
  height?: number | string;
  $maxWidth?: number | string;
  $maxHeight?: number | string;
  ratio?: string;
  radius?: number | string;
}

const skeletonAnimation = keyframes`
  from {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
  to {
    opacity: 1;
  }
`;

const SkeletonBox = styled.div<Props>`
  width: ${({ width }) => width && convertCSS(width)};
  height: ${({ height }) => height && convertCSS(height)};
  max-width: ${({ $maxWidth }) => $maxWidth && convertCSS($maxWidth)};
  max-height: ${({ $maxHeight }) => $maxHeight && convertCSS($maxHeight)};
  aspect-ratio: ${({ ratio }) => ratio};
  border-radius: ${({ radius, theme }) =>
    (radius && convertCSS(radius)) || theme.radius.small};
  background: ${({ theme }) => theme.color.lightGray};
  animation: ${skeletonAnimation} 1s infinite;
`;

export default SkeletonBox;

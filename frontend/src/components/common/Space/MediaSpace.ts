import { styled } from 'styled-components';

import theme from '../../../themes';
import Space from '.';

const getSmallerSpaceSize = (currentIndex: number) => {
  const sizeKeys = theme.spacing;

  if (currentIndex === 0) return sizeKeys[currentIndex];

  return sizeKeys[currentIndex - 1];
};

const MediaSpace = styled(Space)`
  @media (max-width: 744px) {
    min-width: ${({ size }) => getSmallerSpaceSize(size)};
    min-height: ${({ size }) => getSmallerSpaceSize(size)};
  }
`;

export default MediaSpace;

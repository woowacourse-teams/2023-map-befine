import styled from 'styled-components';

import theme from '../../../themes';
import { fontSizeThemeKey } from '../../../themes/fontSize';
import Text from './index';

const getSmallerFontSize = (currentSize: fontSizeThemeKey) => {
  const sizeKeys = Object.keys(theme.fontSize) as fontSizeThemeKey[];
  const currentIndex = sizeKeys.indexOf(currentSize);

  if (currentIndex === -1 || currentIndex === sizeKeys.length - 1) {
    return currentSize;
  }

  return sizeKeys[currentIndex + 1];
};

const MediaText = styled(Text)`
  @media (max-width: 744px) {
    font-size: ${({ $fontSize }) =>
      theme.fontSize[getSmallerFontSize($fontSize)]};
  }
`;

export default MediaText;

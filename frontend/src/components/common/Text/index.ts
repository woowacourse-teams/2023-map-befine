import { styled } from 'styled-components';
import { colorThemeKey } from '../../../themes/color';
import { fontSizeThemeKey } from '../../../themes/fontSize';
import { fontWeightThemeKey } from '../../../themes/fontWeight';
import theme from '../../../themes';

export interface TextProps {
  color: colorThemeKey;
  $fontSize: fontSizeThemeKey;
  $fontWeight: fontWeightThemeKey;
  $textDecoration?: string;
  $textAlign?: string;
  position?: string;
  top?: string;
  right?: string;
  bottom?: string;
  left?: string;
  $zIndex?: string;
  overflow?: string;
  $whiteSpace?: string;
  $wordBreak?: string;
  children?: React.ReactNode;
}

const Text = styled.span<TextProps>`
  color: ${({ color }) => theme.color[color]};
  font-size: ${({ $fontSize }) => theme.fontSize[$fontSize]};
  font-weight: ${({ $fontWeight }) => theme.fontWeight[$fontWeight]};
  text-align: ${({ $textAlign }) => $textAlign};
  display: block;
  letter-spacing: -0.3%;
  line-height: 160%;
`;

export default Text;

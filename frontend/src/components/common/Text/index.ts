import { styled } from 'styled-components';
import { colorThemeKey } from '../../../themes/color';
import { fontSizeThemeKey } from '../../../themes/fontSize';
import { fontWeightThemeKey } from '../../../themes/fontWeight';
import theme from '../../../themes';

export interface TextProps {
  color: colorThemeKey;
  $fontSize: fontSizeThemeKey;
  $fontWeight: fontWeightThemeKey;
  $textAlign?: string;
  children?: React.ReactNode;
}

const Text = styled.span<TextProps>`
  display: block;
  line-height: 160%;
  color: ${({ color }) => theme.color[color]};
  font-size: ${({ $fontSize }) => theme.fontSize[$fontSize]};
  font-weight: ${({ $fontWeight }) => theme.fontWeight[$fontWeight]};
  text-align: ${({ $textAlign }) => $textAlign};
  white-space: pre;
`;

export default Text;

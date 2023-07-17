import { styled } from 'styled-components';
import theme from '../../../themes';
import { colorThemeKey } from '../../../themes/color';
import { SpaceThemeKeys } from '../../../themes/spacing';

export type BoxProps = {
  display?: string;
  width?: string;
  height?: string;
  padding?: SpaceThemeKeys;
  backgroundColor?: colorThemeKey;
  border?: string;
  overflow?: string;
  color?: colorThemeKey;
};

const Box = styled.div<BoxProps>`
  display: ${({ display }) => display ?? 'block'};
  background-color: ${({ backgroundColor }) =>
    backgroundColor && theme.color[backgroundColor]};
  color: ${({ color }) => color && theme.color[color]};
  padding: ${({ padding }) => padding && (theme.spacing[padding] as string)};
  width: ${({ width }) => width};
  height: ${({ height }) => height};
  border: ${({ border }) => border};
  overflow: ${({ overflow }) => overflow};
`;

export default Box;

import { styled } from 'styled-components';
import theme from '../../../themes';
import { colorThemeKey } from '../../../themes/color';
import { SpaceThemeKeys } from '../../../themes/spacing';
import { radiusKey } from '../../../themes/radius';

export interface BoxProps {
  display?: string;
  width?: string;
  height?: string;
  $minWidth?: string;
  $minHeight?: string;
  padding?: SpaceThemeKeys | string;
  $backgroundColor?: colorThemeKey;
  $backdropFilter?: string;
  overflow?: string;
  color?: colorThemeKey;
  position?: string;
  right?: string;
  top?: string;
  left?: string;
  bottom?: string;
  $borderRadius?: radiusKey;
  $borderTop?: string;
  $borderRight?: string;
  $borderBottom?: string;
  $borderLeft?: string;
  cursor?: string;
  opacity?: string;
  $zIndex?: number;
}

const Box = styled.div<BoxProps>`
  display: ${({ display }) => display ?? 'block'};
  background-color: ${({ $backgroundColor }) =>
    $backgroundColor && theme.color[$backgroundColor]};
  backdrop-filter: ${({ $backdropFilter }) => $backdropFilter};
  color: ${({ color }) => color && theme.color[color]};
  padding: ${({ padding }) => padding && convertPadding(padding)};
  width: ${({ width }) => width};
  height: ${({ height }) => height};
  min-width: ${({ $minWidth }) => $minWidth};
  min-height: ${({ $minHeight }) => $minHeight};
  overflow: ${({ overflow }) => overflow};
  position: ${({ position }) => position};
  right: ${({ right }) => right};
  top: ${({ top }) => top};
  left: ${({ left }) => left};
  bottom: ${({ bottom }) => bottom};
  border-radius: ${({ $borderRadius }) =>
    $borderRadius && theme.radius[$borderRadius]};
  border-top: ${({ $borderTop }) => $borderTop};
  border-right: ${({ $borderRight }) => $borderRight};
  border-bottom: ${({ $borderBottom }) => $borderBottom};
  border-left: ${({ $borderLeft }) => $borderLeft};
  cursor: ${({ cursor }) => cursor};
  opacity: ${({ opacity }) => opacity};
  z-index: ${({ $zIndex }) => $zIndex};
`;

const convertPadding = (padding: SpaceThemeKeys | string) => {
  if (typeof padding === 'string' && padding.length > 1) return padding;

  return theme.spacing[Number(padding)];
};

export default Box;

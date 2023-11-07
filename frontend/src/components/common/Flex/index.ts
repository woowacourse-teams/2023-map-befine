import { styled } from 'styled-components';

import theme from '../../../themes';
import { colorThemeKey } from '../../../themes/color';
import { radiusKey } from '../../../themes/radius';
import { SpaceThemeKeys } from '../../../themes/spacing';

interface FlexProps {
  $flexDirection?: string;
  $flexWrap?: string;
  $flexBasis?: string;
  $flexGrow?: string;
  $flexShrink?: string;
  $alignItems?: string;
  $alignContent?: string;
  $justifyContent?: string;
  $justifyItems?: string;
  flex?: string;
  $gap?: string;

  width?: string;
  height?: string;
  $minWidth?: string;
  $minHeight?: string;
  $maxWidth?: string;
  $maxHeight?: string;
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

const Flex = styled.div<FlexProps>`
  display: flex;
  flex-direction: ${({ $flexDirection }) => $flexDirection};
  flex-wrap: ${({ $flexWrap }) => $flexWrap};
  flex-basis: ${({ $flexBasis }) => $flexBasis};
  flex-grow: ${({ $flexGrow }) => $flexGrow};
  flex-shrink: ${({ $flexShrink }) => $flexShrink};
  align-items: ${({ $alignItems }) => $alignItems};
  align-content: ${({ $alignContent }) => $alignContent};
  justify-content: ${({ $justifyContent }) => $justifyContent};
  justify-items: ${({ $justifyItems }) => $justifyItems};
  flex: ${({ flex }) => flex};
  gap: ${({ $gap }) => $gap};

  background-color: ${({ $backgroundColor }) =>
    $backgroundColor && theme.color[$backgroundColor]};
  backdrop-filter: ${({ $backdropFilter }) => $backdropFilter};
  color: ${({ color }) => color && theme.color[color]};
  padding: ${({ padding }) => padding && convertPadding(padding)};
  width: ${({ width }) => width};
  height: ${({ height }) => height};
  min-width: ${({ $minWidth }) => $minWidth};
  min-height: ${({ $minHeight }) => $minHeight};
  max-width: ${({ $maxWidth }) => $maxWidth};
  max-height: ${({ $maxHeight }) => $maxHeight};
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

export default Flex;

import { styled } from 'styled-components';
import theme from '../../../themes';
import { colorThemeKey } from '../../../themes/color';
import { SpaceThemeKeys } from '../../../themes/spacing';
import { radiusKey } from '../../../themes/radius';

export type BoxProps = {
  display?: string;
  width?: string;
  $minWidth?: string;
  height?: string;
  padding?: SpaceThemeKeys;
  $paddingTop?: SpaceThemeKeys;
  $paddingRight?: SpaceThemeKeys;
  $paddingBottom?: SpaceThemeKeys;
  $paddingLeft?: SpaceThemeKeys;
  $backgroundColor?: colorThemeKey;
  border?: string;
  overflow?: string;
  $minHeight?: string;
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
  $backgroundImage?: string;
  $backgroundSize?: string;
  $backdropFilter?: string;
  opacity?: string;
};

const Box = styled.div<BoxProps>`
  display: ${({ display }) => display ?? 'block'};
  background-color: ${({ $backgroundColor }) =>
    $backgroundColor && theme.color[$backgroundColor]};
  color: ${({ color }) => color && theme.color[color]};
  padding: ${({ padding }) => padding && theme.spacing[Number(padding)]};
  padding-top: ${({ $paddingTop }) =>
    $paddingTop && theme.spacing[Number($paddingTop)]};
  padding-right: ${({ $paddingRight }) =>
    $paddingRight && theme.spacing[Number($paddingRight)]};
  padding-bottom: ${({ $paddingBottom }) =>
    $paddingBottom && theme.spacing[Number($paddingBottom)]};
  padding-left: ${({ $paddingLeft }) =>
    $paddingLeft && theme.spacing[Number($paddingLeft)]};
  width: ${({ width }) => width};
  min-width: ${({ $minWidth }) => $minWidth};
  height: ${({ height }) => height};
  border: ${({ border }) => border};
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
  background-image: url(${({ $backgroundImage }) => $backgroundImage});
  background-size: ${({ $backgroundSize }) => $backgroundSize};
  backdrop-filter: ${({ $backdropFilter }) => $backdropFilter};
  opacity: ${({ opacity }) => opacity};
`;
export default Box;

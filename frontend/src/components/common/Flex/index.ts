import { styled } from 'styled-components';
import Box, { BoxProps } from '../Box';

type FlexProps = BoxProps & {
  $flexDirection?: string;
  $flexWrap?: string;
  $flexBasis?: string;
  $flexGrow?: string;
  $flexShrink?: string;
  $alignItems?: string;
  $alignContent?: string;
  $justifyContent?: string;
  $justifyItems?: string;
};

const Flex = styled(Box)<FlexProps>`
  display: flex;
  ${({ $flexDirection }) =>
    $flexDirection && `flex-direction: ${$flexDirection};`}
  ${({ $flexWrap }) => $flexWrap && `flex-wrap: ${$flexWrap};`}
  ${({ $flexBasis }) => $flexBasis && `flex-basis: ${$flexBasis};`}
  ${({ $flexGrow }) => $flexGrow && `flex-grow: ${$flexGrow};`}
  ${({ $flexShrink }) => $flexShrink && `flex-shrink: ${$flexShrink};`}
  ${({ $alignItems }) => $alignItems && `align-items: ${$alignItems};`}
  ${({ $alignContent }) => $alignContent && `align-content: ${$alignContent};`}
  ${({ $justifyContent }) =>
    $justifyContent && `justify-content: ${$justifyContent};`}
  ${({ $justifyItems }) => $justifyItems && `justify-items: ${$justifyItems};`}
`;

export default Flex;

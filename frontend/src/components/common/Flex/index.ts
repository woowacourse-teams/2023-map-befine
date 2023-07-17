import { styled } from 'styled-components';
import Box, { BoxProps } from '../Box';

type FlexProps = BoxProps & {
  flexDirection?: string;
  flexWrap?: string;
  flexBasis?: string;
  flexGrow?: string;
  flexShrink?: string;
  alignItems?: string;
  alignContent?: string;
  justifyContent?: string;
  justifyItems?: string;
};

const Flex = styled(Box) <FlexProps>`
  display: flex;
  ${(props) => props.flexDirection && `flex-direction: ${props.flexDirection};`}
  ${(props) => props.flexWrap && `flex-wrap: ${props.flexWrap};`}
  ${(props) => props.flexBasis && `flex-basis: ${props.flexBasis};`}
  ${(props) => props.flexGrow && `flex-grow: ${props.flexGrow};`}
  ${(props) => props.flexShrink && `flex-shrink: ${props.flexShrink};`}
  ${(props) => props.alignItems && `align-items: ${props.alignItems};`}
  ${(props) => props.alignContent && `align-content: ${props.alignContent};`}
  ${(props) =>
    props.justifyContent && `justify-content: ${props.justifyContent};`}
  ${(props) => props.justifyItems && `justify-items: ${props.justifyItems};`}
`;

export default Flex;

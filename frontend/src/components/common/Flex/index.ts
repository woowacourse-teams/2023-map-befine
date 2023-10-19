import { styled } from 'styled-components';

import Box, { BoxProps } from '../Box';

interface FlexProps extends BoxProps {
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
}

const Flex = styled(Box)<FlexProps>`
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
`;

export default Flex;

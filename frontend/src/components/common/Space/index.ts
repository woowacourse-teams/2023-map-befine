import { styled } from 'styled-components';

import spacing from '../../../themes/spacing';

interface SpaceProps {
  size: 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9;
}

const Space = styled.div<SpaceProps>`
  min-width: ${({ size }) => spacing[size]};
  min-height: ${({ size }) => spacing[size]};
`;

export default Space;

import { styled } from 'styled-components';
import spacing from '../../../themes/spacing';

interface SpaceProps {
  size: 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7;
}

const Space = styled.div<SpaceProps>`
  width: ${({ size }) => spacing[size]};
  height: ${({ size }) => spacing[size]};
`;

export default Space;

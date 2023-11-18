import { keyframes, styled } from 'styled-components';

import Box from '../common/Box';
import Space from '../common/Space';

function TopicInfoSkeleton() {
  return (
    <Box>
      <SkeletonImg />
      <Space size={2} />
      <SkeletonTitle />
      <Space size={4} />
      <SkeletonDescription />
    </Box>
  );
}

const skeletonAnimation = keyframes`
    from {
    opacity: 0.1;
    }
    to {
    opacity: 1;
    }
`;

const SkeletonImg = styled.div`
  width: 332px;
  height: 168px;

  border-radius: 8px;

  background: ${({ theme }) => theme.color.lightGray};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonTitle = styled.div`
  width: 332px;
  height: 32px;

  border-radius: 8px;

  background: ${({ theme }) => theme.color.lightGray};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonDescription = styled(SkeletonTitle)`
  width: 332px;
  height: 80px;
`;

export default TopicInfoSkeleton;

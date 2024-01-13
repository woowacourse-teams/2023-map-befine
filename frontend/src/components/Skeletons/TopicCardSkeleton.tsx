import { keyframes, styled } from 'styled-components';

import Box from '../common/Box';
import Space from '../common/Space';

function TopicCardSkeleton() {
  return (
    <Box>
      <SkeletonImg />
      <Space size={1} />
      <SkeletonTitle />
      <Space size={5} />
      <SkeletonSubTitle />
      <Space size={1} />
      <SkeletonDescription />
    </Box>
  );
}

const skeletonAnimation = keyframes`
  from {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
  to {
    opacity: 1;
  }
`;

const SkeletonImg = styled.div`
  width: 100%;
  max-width: 212px;
  aspect-ratio: 1.6 / 1;

  border-radius: 4px;

  background: ${({ theme }) => theme.color.lightGray};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonTitle = styled.div`
  width: 212px;
  height: 25px;

  border-radius: 4px;

  background: ${({ theme }) => theme.color.lightGray};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonSubTitle = styled(SkeletonTitle)`
  width: 100px;
`;

const SkeletonDescription = styled(SkeletonTitle)`
  height: 46px;
`;

export default TopicCardSkeleton;

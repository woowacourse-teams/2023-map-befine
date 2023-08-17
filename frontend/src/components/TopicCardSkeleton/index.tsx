import { keyframes, styled } from 'styled-components';
import Flex from '../common/Flex';
import Space from '../common/Space';

const TopicCardSkeleton = () => {
  return (
    <Flex $flexDirection="row">
      <SkeletonImg />
      <Space size={2} />
      <Flex $flexDirection="column">
        <SkeletonTitle />
        <Space size={5} />
        <SkeletonDescription />
      </Flex>
    </Flex>
  );
};

const skeletonAnimation = keyframes`
    from {
    opacity: 0.1;
    }
    to {
    opacity: 1;
    }
`;

const SkeletonImg = styled.div`
  width: 138px;
  height: 138px;

  border-radius: 8px;

  background: ${({ theme }) => theme.color['lightGray']};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonTitle = styled.div`
  width: 172px;
  height: 32px;

  border-radius: 8px;

  background: ${({ theme }) => theme.color['lightGray']};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonDescription = styled(SkeletonTitle)`
  height: 80px;
`;

export default TopicCardSkeleton;

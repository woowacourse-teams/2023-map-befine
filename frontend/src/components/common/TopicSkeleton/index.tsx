import { keyframes, styled } from 'styled-components';
import Flex from '../Flex';
import Space from '../Space';

type TopicSkeletonType = 'vertical' | 'horizon';

interface TopicSkeletonProps {
  skeletonType: TopicSkeletonType;
}

const TopicSkeleton = ({ skeletonType }: TopicSkeletonProps) => {
  return (
    <>
      {skeletonType === 'vertical' ? (
        <Flex $flexDirection="column">
          <SkeletonImg />
          <Space size={2} />
          <SkeletonTitle />
          <Space size={2} />
          <SkeletonTitle />
        </Flex>
      ) : (
        <Flex $flexDirection="row">
          <SkeletonImg />
          <Space size={2} />
          <Flex $flexDirection="column">
            <SkeletonTitle />
            <Space size={5} />
            <SkeletonDescription />
          </Flex>
        </Flex>
      )}
    </>
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
  width: 172px;
  height: 172px;

  border-radius: 8px;

  background: ${({ theme }) => theme.color['lightGray']};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonTitle = styled.div`
  width: 172px;
  height: 30px;

  border-radius: 8px;

  background: ${({ theme }) => theme.color['lightGray']};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonDescription = styled(SkeletonTitle)`
  height: 50px;
`;

export default TopicSkeleton;

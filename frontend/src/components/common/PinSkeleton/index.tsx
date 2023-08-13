import { keyframes, styled } from 'styled-components';
import Flex from '../Flex';
import Space from '../Space';

const PinSkeleton = () => {
  return (
    <Flex $flexDirection="column">
      <SkeletonTitle />
      <Space size={2} />
      <SkeletonAddress />
      <Space size={4} />
      <SkeletonDescription />
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

const SkeletonTitle = styled.div`
  width: 320px;
  height: 30px;

  border-radius: 8px;

  background: ${({ theme }) => theme.color['lightGray']};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonAddress = styled(SkeletonTitle)`
  width: 320px;
  height: 15px;
`;

const SkeletonDescription = styled(SkeletonTitle)`
  width: 320px;
  height: 70px;
`;

export default PinSkeleton;

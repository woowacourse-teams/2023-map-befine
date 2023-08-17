import { keyframes, styled } from 'styled-components';
import Flex from '../common/Flex';
import Space from '../common/Space';

const PinPreviewSkeleton = () => {
  return (
    <Flex $flexDirection="column">
      <SkeletonTitle />
      <Space size={1} />
      <SkeletonAddress />
      <Space size={3} />
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
  height: 28px;

  border-radius: 8px;

  background: ${({ theme }) => theme.color['lightGray']};
  animation: ${skeletonAnimation} 1s infinite;
`;

const SkeletonAddress = styled(SkeletonTitle)`
  width: 320px;
  height: 20px;
`;

const SkeletonDescription = styled(SkeletonTitle)`
  width: 320px;
  height: 62px;
`;

export default PinPreviewSkeleton;

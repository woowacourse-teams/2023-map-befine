import Flex from '../common/Flex';
import PinPreviewSkeleton from '../PinPreviewSkeleton';
import Space from '../common/Space';
import TopicInfoSkeleton from '../TopicInfoSkeleton';

const PinsOfTopicSkeleton = () => {
  return (
    <Flex $flexDirection="column">
      <Space size={0} />
      <TopicInfoSkeleton />
      <Space size={7} />
      <PinPreviewSkeleton />
      <Space size={6} />
      <PinPreviewSkeleton />
      <Space size={6} />
      <PinPreviewSkeleton />
    </Flex>
  );
};

export default PinsOfTopicSkeleton;

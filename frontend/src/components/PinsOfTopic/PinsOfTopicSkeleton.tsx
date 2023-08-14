import Flex from '../common/Flex';
import PinSkeleton from '../common/PinSkeleton';
import Space from '../common/Space';
import TopicSkeleton from '../common/TopicSkeleton';

const PinsOfTopicSkeleton = () => {
  return (
    <Flex $flexDirection="column">
      <Space size={7} />
      <TopicSkeleton />
      <Space size={7} />
      <PinSkeleton />
      <Space size={6} />
      <PinSkeleton />
      <Space size={6} />
      <PinSkeleton />
    </Flex>
  );
};

export default PinsOfTopicSkeleton;

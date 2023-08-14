import Flex from '../common/Flex';
import Space from '../common/Space';
import TopicSkeleton from '../common/TopicSkeleton';

const SeeAllCardListSkeleton = () => {
  return (
    <Flex $flexDirection="column">
      <TopicSkeleton />
      <Space size={3} />
      <TopicSkeleton />
      <Space size={3} />
      <TopicSkeleton />
      <Space size={3} />
      <TopicSkeleton />
    </Flex>
  );
};

export default SeeAllCardListSkeleton;

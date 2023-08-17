import Flex from '../common/Flex';
import Space from '../common/Space';
import TopicCardSkeleton from '../TopicCardSkeleton';

const SeeAllCardListSkeleton = () => {
  return (
    <Flex $flexDirection="column">
      <TopicCardSkeleton />
      <Space size={3} />
      <TopicCardSkeleton />
      <Space size={3} />
      <TopicCardSkeleton />
      <Space size={3} />
      <TopicCardSkeleton />
    </Flex>
  );
};

export default SeeAllCardListSkeleton;

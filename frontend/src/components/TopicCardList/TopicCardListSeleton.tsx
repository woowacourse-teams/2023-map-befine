import Flex from '../common/Flex';
import Space from '../common/Space';
import TopicSkeleton from '../common/TopicSkeleton';

const TopicCardListSeleton = () => {
  return (
    <Flex>
      <TopicSkeleton />
      <Space size={3} />
      <TopicSkeleton />
    </Flex>
  );
};

export default TopicCardListSeleton;

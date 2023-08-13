import Flex from '../common/Flex';
import Space from '../common/Space';
import TopicSkeleton from '../common/TopicSkeleton';

const TopicCardListSeleton = () => {
  return (
    <Flex>
      <TopicSkeleton skeletonType="vertical" />
      <Space size={3} />
      <TopicSkeleton skeletonType="vertical" />
    </Flex>
  );
};

export default TopicCardListSeleton;

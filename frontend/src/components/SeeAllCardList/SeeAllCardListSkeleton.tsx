import Flex from '../common/Flex';
import Space from '../common/Space';
import TopicSkeleton from '../common/TopicSkeleton';

const SeeAllCardListSkeleton = () => {
  return (
    <Flex $flexDirection="column">
      <TopicSkeleton skeletonType="horizon" />
      <Space size={3} />
      <TopicSkeleton skeletonType="horizon" />
      <Space size={3} />
      <TopicSkeleton skeletonType="horizon" />
      <Space size={3} />
      <TopicSkeleton skeletonType="horizon" />
    </Flex>
  );
};

export default SeeAllCardListSkeleton;

import { styled } from 'styled-components';
import TopicCardSkeleton from '../TopicCardSkeleton';

const TopicCardListSkeleton = () => {
  return (
    <Wrapper>
      <TopicCardSkeleton />
      <TopicCardSkeleton />
      <TopicCardSkeleton />
      <TopicCardSkeleton />
      <TopicCardSkeleton />
      <TopicCardSkeleton />
    </Wrapper>
  );
};

const Wrapper = styled.section`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  width: 1036px;
  height: 300px;
`;

export default TopicCardListSkeleton;

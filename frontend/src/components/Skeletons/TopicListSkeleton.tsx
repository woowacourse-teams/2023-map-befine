import { styled } from 'styled-components';

import TopicCardSkeleton from './TopicCardSkeleton';

function TopicListSkeleton() {
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
}

const Wrapper = styled.section`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  width: 1140px;
`;

export default TopicListSkeleton;

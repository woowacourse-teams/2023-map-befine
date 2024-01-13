import { styled } from 'styled-components';

import Space from '../common/Space';
import TopicCardSkeleton from './TopicCardSkeleton';

function TopicListSkeleton() {
  return (
    <>
      <TopicCardWrapper>
        <TopicCardSkeleton />
        <TopicCardSkeleton />
        <TopicCardSkeleton />
        <TopicCardSkeleton />
        <TopicCardSkeleton />
      </TopicCardWrapper>
      <Space size={4} />
      <TopicCardWrapper>
        <TopicCardSkeleton />
        <TopicCardSkeleton />
        <TopicCardSkeleton />
        <TopicCardSkeleton />
        <TopicCardSkeleton />
      </TopicCardWrapper>
    </>
  );
}

const TopicCardWrapper = styled.section`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  width: 1140px;
`;

export default TopicListSkeleton;

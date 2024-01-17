import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';

import useGet from '../../apiHooks/useGet';
import { TopicCardProps } from '../../types/Topic';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Grid from '../common/Grid';
import Space from '../common/Space';
import Text from '../common/Text';
import TopicCard from '../TopicCard';
import useProfileList from '../../hooks/queries/useProfileList';

interface TopicCardListProps {
  url: string;
  errorMessage: string;
  commentWhenEmpty: string;
  pageCommentWhenEmpty: string;
  routePage: () => void;
  children?: React.ReactNode;
}

function TopicCardList({
  url,
  errorMessage,
  commentWhenEmpty,
  pageCommentWhenEmpty,
  routePage,
  children,
}: TopicCardListProps) {
  const [topics, setTopics] = useState<TopicCardProps[] | null>(null);
  const { fetchGet } = useGet();
  const { data } = useProfileList();

  const getTopicsFromServer = async () => {
    if (data !== undefined) {
      setTopics(data);
    }
  };

  useEffect(() => {
    getTopicsFromServer();
  }, []);

  if (!topics) return null;

  if (topics.length === 0) {
    return (
      <EmptyWrapper>
        <Flex $alignItems="center">
          {children}
          <Space size={1} />
          <Text color="black" $fontSize="default" $fontWeight="normal">
            {commentWhenEmpty}
          </Text>
          <Space size={4} />
        </Flex>
        <Space size={5} />
        <Button variant="primary" onClick={routePage}>
          {pageCommentWhenEmpty}
        </Button>
      </EmptyWrapper>
    );
  }

  return (
    <Wrapper>
      <Grid
        rows="auto"
        columns={5}
        gap={20}
        width="100%"
        $mediaQueries={[
          [1180, 4],
          [900, 3],
          [660, 2],
          [320, 1],
        ]}
      >
        {topics.map((topic) => (
          <ul key={topic.id}>
            <TopicCard
              cardType="default"
              id={topic.id}
              image={topic.image}
              name={topic.name}
              creator={topic.creator}
              updatedAt={topic.updatedAt}
              pinCount={topic.pinCount}
              bookmarkCount={topic.bookmarkCount}
              isInAtlas={topic.isInAtlas}
              isBookmarked={topic.isBookmarked}
              getTopicsFromServer={getTopicsFromServer}
            />
          </ul>
        ))}
      </Grid>
    </Wrapper>
  );
}

const EmptyWrapper = styled.section`
  height: 240px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Wrapper = styled.section`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;

export default TopicCardList;

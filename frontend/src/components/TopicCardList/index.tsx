import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import TopicCard from '../TopicCard';
import { TopicCardProps } from '../../types/Topic';
import useGet from '../../apiHooks/useGet';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';
import Button from '../common/Button';

interface TopicCardListProps {
  url: string;
  errorMessage: string;
  commentWhenEmpty: string;
  pageCommentWhenEmpty: string;
  routePage: () => void;
  children?: React.ReactNode;
}

const TopicCardList = ({
  url,
  errorMessage,
  commentWhenEmpty,
  pageCommentWhenEmpty,
  routePage,
  children,
}: TopicCardListProps) => {
  const [topics, setTopics] = useState<TopicCardProps[] | null>(null);
  const { fetchGet } = useGet();

  const getTopicsFromServer = async () => {
    fetchGet<TopicCardProps[]>(url, errorMessage, (response) => {
      setTopics(response);
    });
  };

  useEffect(() => {
    getTopicsFromServer();
  }, []);

  if (!topics) return <></>;

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
      {topics.map((topic) => (
        <Fragment key={topic.id}>
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
        </Fragment>
      ))}
    </Wrapper>
  );
};

const EmptyWrapper = styled.section`
  height: 240px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Wrapper = styled.ul`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;

export default TopicCardList;

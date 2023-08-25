import { Fragment, useEffect, useState } from 'react';
import { TopicCardProps } from '../../types/Topic';
import { getApi } from '../../apis/getApi';
import TopicCard from '../TopicCard';
import Flex from '../common/Flex';
import { styled } from 'styled-components';

interface SeeAllCardListProps {
  url: string;
}

const SeeAllCardList = ({ url }: SeeAllCardListProps) => {
  const [topics, setTopics] = useState<TopicCardProps[]>([]);

  const getAndSetDataFromServer = async () => {
    const topics = await getApi<TopicCardProps[]>(url);
    setTopics(topics);
  };

  useEffect(() => {
    getAndSetDataFromServer();
  }, []);

  return (
    <Wrapper>
      {topics &&
        topics.map((topic) => (
          <Fragment key={topic.id}>
            <TopicCard
              id={topic.id}
              image={topic.image}
              name={topic.name}
              creator={topic.creator}
              pinCount={topic.pinCount}
              bookmarkCount={topic.bookmarkCount}
              updatedAt={topic.updatedAt}
              isInAtlas={topic.isInAtlas}
              isBookmarked={topic.isBookmarked}
              setTopicsFromServer={getAndSetDataFromServer}
            />
          </Fragment>
        ))}
    </Wrapper>
  );
};

const Wrapper = styled.ul`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;

export default SeeAllCardList;

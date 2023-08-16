import { Fragment, useEffect, useState } from 'react';
import { TopicType } from '../../types/Topic';
import { getApi } from '../../apis/getApi';
import TopicCard from '../TopicCard';
import Flex from '../common/Flex';

interface SeeAllCardListProps {
  url: string;
}

const SeeAllCardList = ({ url }: SeeAllCardListProps) => {
  const [topics, setTopics] = useState<TopicType[]>([]);

  const getAndSetDataFromServer = async () => {
    const topics = await getApi<TopicType[]>('default', url);
    setTopics(topics);
  };

  useEffect(() => {
    getAndSetDataFromServer();
  }, []);

  return (
    <ul>
      <Flex $flexWrap="wrap" $gap="20px" overflow="hidden">
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
      </Flex>
    </ul>
  );
};

export default SeeAllCardList;

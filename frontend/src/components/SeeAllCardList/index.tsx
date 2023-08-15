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
                topicId={topic.id}
                topicImage={topic.image}
                topicTitle={topic.name}
                topicPinCount={topic.pinCount}
                topicUpdatedAt={topic.updatedAt}
              />
            </Fragment>
          ))}
      </Flex>
    </ul>
  );
};

export default SeeAllCardList;

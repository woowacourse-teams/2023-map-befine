import { Fragment, useEffect, useState } from 'react';
import { TopicType } from '../../types/Topic';
import { getApi } from '../../apis/getApi';
import TopicCard from '../TopicCard';
import Space from '../common/Space';

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
      {topics &&
        topics.map((topic, idx) => (
          <Fragment key={topic.id}>
            <TopicCard
              topicShape="horizontal"
              topicId={topic.id}
              topicImage={topic.image}
              topicTitle={topic.name}
              topicPinCount={topic.pinCount}
              topicUpdatedAt={topic.updatedAt}
            />
            {topics.length - 1 !== idx ? <Space size={4} /> : <></>}
          </Fragment>
        ))}
    </ul>
  );
};

export default SeeAllCardList;

import { Fragment, useContext, useEffect, useState } from 'react';
import { getApi } from '../../apis/getApi';
import { TopicType } from '../../types/Topic';
import TopicCard from '../TopicCard';
import Space from '../common/Space';
import { MarkerContext } from '../../context/MarkerContext';
import Flex from '../common/Flex';
import { useLocation } from 'react-router-dom';

const TopicCardList = () => {
  const [topics, setTopics] = useState<TopicType[]>([]);
  const { markers, removeMarkers } = useContext(MarkerContext);
  const { state: url } = useLocation();

  const getAndSetDataFromServer = async () => {
    const topics = url
      ? await getApi<TopicType[]>('default', url)
      : await getApi<TopicType[]>('default', '/topics');
    setTopics(topics);
  };

  useEffect(() => {
    getAndSetDataFromServer();
    if (markers.length > 0) removeMarkers();
  }, []);

  return (
    <ul>
      <Flex width="360px" height="292px" overflow="auto">
        {topics &&
          topics.map((topic, index) => {
            return (
              <Fragment key={index}>
                <TopicCard
                  topicShape="vertical"
                  topicId={topic.id}
                  topicImage={topic.image}
                  topicTitle={topic.name}
                  topicUpdatedAt={topic.updatedAt}
                  topicPinCount={topic.pinCount}
                />
                {topics.length - 1 !== index ? <Space size={4} /> : <></>}
              </Fragment>
            );
          })}
      </Flex>
    </ul>
  );
};

export default TopicCardList;

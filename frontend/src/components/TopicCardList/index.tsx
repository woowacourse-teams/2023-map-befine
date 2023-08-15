import { Fragment, useContext, useEffect, useState } from 'react';
import { getApi } from '../../apis/getApi';
import { TopicType } from '../../types/Topic';
import TopicCard from '../TopicCard';
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
      <Flex height="300px" $flexWrap="wrap" $gap="20px" overflow="hidden">
        {topics &&
          topics.map((topic, index) => {
            return (
              index < 6 && (
                <Fragment key={topic.id}>
                  <TopicCard
                    topicId={topic.id}
                    topicImage={topic.image}
                    topicTitle={topic.name}
                    topicUpdatedAt={topic.updatedAt}
                    topicPinCount={topic.pinCount}
                  />
                </Fragment>
              )
            );
          })}
      </Flex>
    </ul>
  );
};

export default TopicCardList;

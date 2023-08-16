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

  const setTopicsFromServer = async () => {
    const topics = await getApi<TopicType[]>('default', '/topics');

    setTopics(topics);
  };

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
                    id={topic.id}
                    image={topic.image}
                    name={topic.name}
                    creator={topic.creator}
                    updatedAt={topic.updatedAt}
                    pinCount={topic.pinCount}
                    bookmarkCount={topic.bookmarkCount}
                    isInAtlas={topic.isInAtlas}
                    isBookmarked={topic.isBookmarked}
                    setTopicsFromServer={setTopicsFromServer}
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

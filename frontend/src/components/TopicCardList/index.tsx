import { Fragment, useContext, useEffect, useState } from 'react';
import { getApi } from '../../apis/getApi';
import { TopicType } from '../../types/Topic';
import TopicCard from '../TopicCard';
import { MarkerContext } from '../../context/MarkerContext';
import Flex from '../common/Flex';
import { useLocation } from 'react-router-dom';
import useToast from '../../hooks/useToast';

const TopicCardList = () => {
  const [topics, setTopics] = useState<TopicType[]>([]);
  const { markers, removeMarkers, removeInfowindows } =
    useContext(MarkerContext);
  const { state: url } = useLocation();
  const { showToast } = useToast();

  const getAndSetDataFromServer = async () => {
    try {
      const topics = url
        ? await getApi<TopicType[]>('default', url)
        : await getApi<TopicType[]>('default', '/topics');

      setTopics(topics);
    } catch {
      showToast(
        'error',
        '로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인 해주세요.',
      );
    }
  };

  useEffect(() => {
    getAndSetDataFromServer();
    if (markers.length > 0) {
      removeMarkers();
      removeInfowindows();
    }
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
                    setTopicsFromServer={getAndSetDataFromServer}
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

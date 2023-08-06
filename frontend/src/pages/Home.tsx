import Space from '../components/common/Space';
import Text from '../components/common/Text';
import TopicCard from '../components/TopicCard';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Box from '../components/common/Box';
import { Fragment, useContext, useEffect, useState } from 'react';
import { getApi } from '../apis/getApi';
import { TopicType } from '../types/Topic';
import useNavigator from '../hooks/useNavigator';
import { MergeOrSeeTogether } from '../components/MergeOrSeeTogether';
import { MarkerContext } from '../context/MarkerContext';

const Home = () => {
  const [topics, setTopics] = useState<TopicType[]>([]);
  const [taggedTopicIds, setTaggedTopicIds] = useState<number[]>([]);
  const [tagTopics, setTagTopics] = useState<string[]>([]);
  const { markers, removeMarkers } = useContext(MarkerContext);
  const { routePage } = useNavigator();

  const goToNewTopic = () => {
    routePage('new-topic', taggedTopicIds);
  };

  const goToSeveralTopic = () => {
    routePage(`topics/${taggedTopicIds.join(',')}`, taggedTopicIds.join(','));
  };

  const onTagCancel = () => {
    setTagTopics([]);
    setTaggedTopicIds([]);
  };

  const getAndSetDataFromServer = async () => {
    const topics = await getApi('default', '/topics');
    setTopics(topics);
  };

  // 현재 위치 받아오기
  useEffect(() => {
    getAndSetDataFromServer();
    if (markers.length > 0) removeMarkers();
  }, []);

  return (
    <Box position="relative">
      <Space size={2} />
      {tagTopics.length > 0 ? (
        <MergeOrSeeTogether
          tag={tagTopics}
          onClickConfirm={goToSeveralTopic}
          onClickClose={onTagCancel}
          confirmButton="같이보기"
        />
      ) : null}
      <Space size={4} />
      <Text color="black" $fontSize="large" $fontWeight="bold" tabIndex={0}>
        내 주변 인기 있는 토픽
      </Text>
      <Space size={2} />
      <ul>
        {topics &&
          topics.map((topic, index) => {
            return (
              <Fragment key={index}>
                <TopicCard
                  topicId={topic.id}
                  topicImage={topic.image}
                  topicTitle={topic.name}
                  topicUpdatedAt={topic.updatedAt}
                  topicPinCount={topic.pinCount}
                  tagTopics={tagTopics}
                  setTagTopics={setTagTopics}
                  taggedTopicIds={taggedTopicIds}
                  setTaggedTopicIds={setTaggedTopicIds}
                />
                <Space size={4} />
              </Fragment>
            );
          })}
      </ul>
      <Flex position="fixed" bottom="40px" left="142px">
        <Button variant="primary" onClick={goToNewTopic}>
          {tagTopics.length > 0 ? '토픽 병합하기' : '토픽 추가하기'}
        </Button>
      </Flex>
    </Box>
  );
};

export default Home;

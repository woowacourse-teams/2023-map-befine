import Space from '../components/common/Space';
import Text from '../components/common/Text';
import TopicCard from '../components/TopicCard';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Box from '../components/common/Box';
import { Fragment, useContext, useEffect, useState } from 'react';
import { getApi } from '../utils/getApi';
import { TopicType } from '../types/Topic';
import useNavigator from '../hooks/useNavigator';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { MergeOrSeeTogether } from '../components/MergeOrSeeTogether';
import { TagIdContext } from '../store/TagId';

const Home = () => {
  const [topics, setTopics] = useState<TopicType[]>([]);
  const [tagTopics, setTagTopics] = useState<string[]>([]);
  const { routePage } = useNavigator();
  const { setCoordinates } = useContext(CoordinatesContext);

  const { tagId, setTagId } = useContext(TagIdContext);

  const goToNewTopic = () => {
    routePage('new-topic', 'topics');
  };

  const goToSeveralTopic = () => {
    routePage(`topics/${tagId}`, tagId);
  };

  const onTagCancel = () => {
    setTagTopics([]);
    setTagId([]);
  };

  const getAndSetDataFromServer = async () => {
    const topics = await getApi('/topics');
    setTopics(topics);
  };

  // 현재 위치 받아오기

  useEffect(() => {
    getAndSetDataFromServer();

    setCoordinates([
      { latitude: String(37.5055), longitude: String(127.0509) },
    ]);
  }, []);

  useEffect(() => {
    if (topics.length === 0) setTagId([]);
  }, [topics]);

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
      <Text color="black" $fontSize="large" $fontWeight="bold">
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
                />
                <Space size={4} />
              </Fragment>
            );
          })}
      </ul>
      <Flex position="fixed" bottom="40px" left="130px">
        <Button variant="primary" onClick={goToNewTopic}>
          {tagId.length > 0 ? '토픽 병합하기' : '토픽 추가하기'}
        </Button>
      </Flex>
    </Box>
  );
};

export default Home;

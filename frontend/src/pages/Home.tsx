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

const Home = () => {
  const [topics, setTopics] = useState<TopicType[]>([]);
  const { routePage } = useNavigator();
  const { setCoordinates } = useContext(CoordinatesContext);

  const goToNewTopic = () => {
    routePage('new-topic');
  };

  const getAndSetDataFromServer = async () => {
    const topics = await getApi('/');
    setTopics(topics);
  };

  // 현재 위치 받아오기

  useEffect(() => {
    getAndSetDataFromServer();

    setCoordinates([{ latitude: 37.5055, longitude: 127.0509 }]);
  }, []);

  return (
    <Box position="relative">
      <Space size={6} />
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
                  topicEmoji={topic.emoji}
                  topicTitle={topic.name}
                  topicUpdatedAt={topic.updatedAt}
                  topicPinCount={topic.pinCount}
                />
                <Space size={4} />
              </Fragment>
            );
          })}
      </ul>
      <Flex position="fixed" bottom="40px" left="130px">
        <Button variant="primary" onClick={goToNewTopic}>
          토픽 추가하기
        </Button>
      </Flex>
    </Box>
  );
};

export default Home;

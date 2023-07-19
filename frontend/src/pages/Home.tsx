import Space from '../components/common/Space';
import Text from '../components/common/Text';
import TopicCard from '../components/TopicCard';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Box from '../components/common/Box';
import { Fragment, useEffect, useState } from 'react';
import { getApi } from '../utils/getApi';
import { useNavigate } from 'react-router-dom';
import { TopicType } from '../types/Topic';

const Home = () => {
  const [topics, setTopics] = useState<TopicType[]>([]);

  const navigator = useNavigate();

  const getAndSetDataFromServer = async () => {
    const data = await getApi('/');
    setTopics(data);
  };

  const goToNewTopic = () => {
    navigator('new-topic');
  };

  useEffect(() => {
    getAndSetDataFromServer();
  }, []);

  return (
    <Box position="relative">
      <Space size={6} />
      <Text color="black" $fontSize="large" $fontWeight="bold">
        내 주변 인기 있는 토픽
      </Text>
      <Space size={2} />
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

      <Flex position="fixed" bottom="40px" left="130px">
        <Button variant="primary" onClick={goToNewTopic}>
          토픽 추가하기
        </Button>
      </Flex>
    </Box>
  );
};

export default Home;

import Space from '../components/common/Space';
import Text from '../components/common/Text';
import TopicCard from '../components/TopicCard';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Box from '../components/common/Box';
import { Fragment } from 'react';

const data = [
  {
    topicEmoji: '🍛',
    topicTitle: '선릉 직장인이 추천하는 맛집',
    topicInformation: '업데이트 : 07.05 | 핀 개수 : 57',
  },
  {
    topicEmoji: '💪',
    topicTitle: '산스장 모음',
    topicInformation: '업데이트 : 22.12.25 | 핀 개수 : 257',
  },
  {
    topicEmoji: '✈️',
    topicTitle: '서울 여행하기 좋은 곳',
    topicInformation: '업데이트 : 01.25 | 핀 개수 : 9',
  },
];

const Home = () => {
  return (
    <Box position="relative">
      <Space size={6} />
      <Text color="black" $fontSize="large" $fontWeight="bold">
        내 주변 인기 있는 토픽
      </Text>
      <Space size={2} />
      {data &&
        data.map((topic, index) => {
          return (
            // TODO: topics/${topicId}
            <Fragment key={index}>
              <TopicCard
                topicEmoji={topic.topicEmoji}
                topicTitle={topic.topicTitle}
                topicInformation={topic.topicInformation}
              />
              <Space size={4} />
            </Fragment>
          );
        })}

      <Flex position="fixed" bottom="40px" left="130px">
        <Button variant="primary">토픽 추가하기</Button>
      </Flex>
    </Box>
  );
};

export default Home;

import Space from '../components/common/Space';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import TopicCardContainer from '../components/TopicCardContainer';

const Home = () => {
  const { routePage } = useNavigator();

  return (
    <Box position="relative">
      <section>
        <Space size={4} />
        <Flex $justifyContent="space-between" $alignItems="baseline">
          <Text color="black" $fontSize="large" $fontWeight="bold" tabIndex={0}>
            인기 급상승한 지도
          </Text>
          <Text
            color="gray"
            $fontSize="small"
            $fontWeight="normal"
            tabIndex={1}
          >
            전체 보기
          </Text>
        </Flex>
        <TopicCardContainer />
      </section>
      <section>
        <Space size={4} />
        <Flex $justifyContent="space-between" $alignItems="baseline">
          <Text color="black" $fontSize="large" $fontWeight="bold" tabIndex={0}>
            내 주변인 지도
          </Text>
          <Text
            color="gray"
            $fontSize="small"
            $fontWeight="normal"
            tabIndex={2}
          >
            전체 보기
          </Text>
        </Flex>
        <TopicCardContainer />
      </section>
      <section>
        <Space size={4} />
        <Flex $justifyContent="space-between" $alignItems="baseline">
          <Text color="black" $fontSize="large" $fontWeight="bold" tabIndex={0}>
            새로운 지도
          </Text>
          <Text
            color="gray"
            $fontSize="small"
            $fontWeight="normal"
            tabIndex={3}
          >
            전체 보기
          </Text>
        </Flex>
        <TopicCardContainer />
      </section>
      <Space size={2} />
    </Box>
  );
};

export default Home;
